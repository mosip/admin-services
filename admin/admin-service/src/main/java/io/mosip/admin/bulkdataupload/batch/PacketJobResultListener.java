package io.mosip.admin.bulkdataupload.batch;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacketJobResultListener implements JobExecutionListener {
	 private static final Logger logger = LoggerFactory.getLogger(JobResultListener.class);
	    private static String STATUS_MESSAGE = " <br/> READ: %d, STATUS: %s, MESSAGE: %s";
	    private AuditUtil auditUtil;
	    private BulkUploadTranscationRepository bulkUploadTranscationRepository;
	    private static final String SECURITY_PARAM_NAME = "security-param";
	    private static final ThreadLocal<Authentication> ORIGINAL_CONTEXT = new ThreadLocal<>();

	    public PacketJobResultListener(BulkUploadTranscationRepository bulkUploadTranscationRepository,
	                             AuditUtil auditUtil) {
	        this.bulkUploadTranscationRepository = bulkUploadTranscationRepository;
	        this.auditUtil = auditUtil;
	    }

	    @Override
	    public void beforeJob(JobExecution jobExecution) {
	    }

	    @Override
	    public void afterJob(JobExecution jobExecution) {
	        String jobId = jobExecution.getJobParameters().getString("transactionId");
	        logger.info("Job completed : {}", jobId);
	        try {
	            List<String> failures = new ArrayList<String>();
	            jobExecution.getStepExecutions().forEach(step -> {
	                step.getFailureExceptions().forEach(failure -> {
	                    if (failure instanceof FlatFileParseException) {
	                        failures.add("Line --> " + ((FlatFileParseException) failure).getLineNumber() +
	                                " --> Datatype mismatch / Failed to write into object");
	                    } else
	                        failures.add(failure.getCause() != null ? failure.getCause().getMessage() : failure.getMessage());
	                });
	            });

	            Optional<Integer> commitResult = jobExecution.getStepExecutions().stream().map(StepExecution::getCommitCount).findFirst();
	            Optional<Integer> readResult = jobExecution.getStepExecutions().stream().map(StepExecution::getCommitCount).findFirst();
	            int readCount = readResult.isPresent() ? readResult.get() : 0;
	            int commitCount = commitResult.isPresent() ? commitResult.get() : 0;
	            String message = String.format(STATUS_MESSAGE, commitCount,
	                    jobExecution.getStatus().toString(), failures.isEmpty() ? "0 Errors" : failures.toString());
	            auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_COMPLETED,
	                            jobId + " --> " + message), jobExecution.getJobParameters().getString("username"));
	            bulkUploadTranscationRepository.updateBulkUploadTransactionPacket(jobId, jobExecution.getExitStatus().getExitCode(), commitCount, message);
	            

	        } catch (Throwable t) {
	            logger.error("Failed  to update job status {}", jobId, t);
	        }
	    }

}
