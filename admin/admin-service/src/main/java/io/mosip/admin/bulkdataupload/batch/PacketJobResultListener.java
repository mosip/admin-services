package io.mosip.admin.bulkdataupload.batch;

import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import org.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacketJobResultListener implements JobExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(PacketJobResultListener.class);

	private static final String STATUS_MESSAGE = " <br/> READ: %d, STATUS: %s, MESSAGE: %s";

	private final AuditUtil auditUtil;

	private final JobExecutionSecurityContextListener jobExecutionSecurityContextListener;

	private final BulkUploadTranscationRepository bulkUploadTranscationRepository;

	private static final String SECURITY_PARAM_NAME = "security-param";
	private static final ThreadLocal<Authentication> ORIGINAL_CONTEXT = new ThreadLocal<>();

	public PacketJobResultListener(
			BulkUploadTranscationRepository bulkUploadTranscationRepository,
			AuditUtil auditUtil,
			JobExecutionSecurityContextListener jobExecutionSecurityContextListener) {

		this.bulkUploadTranscationRepository = bulkUploadTranscationRepository;
		this.auditUtil = auditUtil;
		this.jobExecutionSecurityContextListener = jobExecutionSecurityContextListener;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		String jobId = jobExecution.getJobParameters().getString("transactionId");
		logger.info("Job started : {}", jobId);

		this.jobExecutionSecurityContextListener.restoreContext(jobExecution);

		if (jobExecution.getExecutionContext().containsKey(SECURITY_PARAM_NAME)) {
			Authentication authentication =
					(Authentication) jobExecution.getExecutionContext().get(SECURITY_PARAM_NAME);

			SecurityContext securityContext = SecurityContextHolder.getContext();
			ORIGINAL_CONTEXT.set(securityContext.getAuthentication());
			securityContext.setAuthentication(authentication);
		} else {
			logger.error("Missing security-param in job context for job {}", jobId);
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		String jobId = jobExecution.getJobParameters().getString("transactionId");
		logger.info("Job completed : {}", jobId);

		try {
			List<String> failures = new ArrayList<>();

			jobExecution.getStepExecutions().forEach(step ->
					step.getFailureExceptions().forEach(failure -> {
						if (failure instanceof FlatFileParseException f) {
							failures.add("Line --> " + f.getLineNumber()
									+ " --> Datatype mismatch / Failed to write into object");
						} else {
							failures.add(failure.getCause() != null
									? failure.getCause().getMessage()
									: failure.getMessage());
						}
					})
			);

			Optional<Long> commitResult =
					jobExecution.getStepExecutions().stream()
							.map(StepExecution::getCommitCount)
							.findFirst();

			long commitCount = commitResult.orElse(0L);

			String message = String.format(
					STATUS_MESSAGE,
					commitCount,
					jobExecution.getStatus().toString(),
					failures.isEmpty() ? "0 Errors" : failures.toString()
			);

			auditUtil.setAuditRequestDto(
					EventEnum.getEventEnumWithValue(
							EventEnum.BULKDATA_UPLOAD_COMPLETED,
							jobId + " --> " + message),
					jobExecution.getJobParameters().getString("username")
			);

			bulkUploadTranscationRepository.updateBulkUploadTransactionPacket(
					jobId,
					jobExecution.getExitStatus().getExitCode(),
					Math.toIntExact(commitCount),
					message
			);

		} catch (Throwable t) {
			logger.error("Failed to update job status {}", jobId, t);
		} finally {
			clearContext();
			this.jobExecutionSecurityContextListener.clearContext(jobExecution);
		}
	}

	private void clearContext() {
		SecurityContextHolder.clearContext();

		Authentication originalAuth = ORIGINAL_CONTEXT.get();
		if (originalAuth != null) {
			SecurityContextHolder.getContext().setAuthentication(originalAuth);
			ORIGINAL_CONTEXT.remove();
		}
	}
}
