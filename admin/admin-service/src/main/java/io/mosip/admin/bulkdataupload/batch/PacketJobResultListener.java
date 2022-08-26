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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;

import java.math.BigInteger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacketJobResultListener implements JobExecutionListener {
	 private static final Logger logger = LoggerFactory.getLogger(PacketJobResultListener.class);
	    private static String STATUS_MESSAGE = " <br/> READ: %d, STATUS: %s, MESSAGE: %s";
	    private AuditUtil auditUtil;
	    private JobExecutionSecurityContextListener jobExecutionSecurityContextListener;
	    private BulkUploadTranscationRepository bulkUploadTranscationRepository;
	    private static final String SECURITY_PARAM_NAME = "security-param";
	    private static final ThreadLocal<Authentication> ORIGINAL_CONTEXT = new ThreadLocal<>();

	    public PacketJobResultListener(BulkUploadTranscationRepository bulkUploadTranscationRepository,
	                             AuditUtil auditUtil, JobExecutionSecurityContextListener jobExecutionSecurityContextListener) {
	        this.bulkUploadTranscationRepository = bulkUploadTranscationRepository;
	        this.auditUtil = auditUtil;
	        this.jobExecutionSecurityContextListener = new JobExecutionSecurityContextListener();
	    }

	    @Override
	    public void beforeJob(JobExecution jobExecution) {
	    	System.out.println("before job started");
	        logger.info("Job started : {}", jobExecution.getJobParameters().getString("transactionId"));
	        this.jobExecutionSecurityContextListener.fillJobExecutionContext(jobExecution);

	        if(jobExecution.getStepExecutions().isEmpty()) {
	        	System.out.println("restore context starting");
	            restoreContext(jobExecution);
	            System.out.println("restoring context done");
	        }
	        else {
	            for(StepExecution stepExecution : jobExecution.getStepExecutions()) {
	            	System.out.println("restoring context using step execution");
	                this.jobExecutionSecurityContextListener.restoreContext(stepExecution);
	            }
	        }
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
	                            jobId + " --> " + message));
	            bulkUploadTranscationRepository.updateBulkUploadTransactionPacket(jobId, jobExecution.getExitStatus().getExitCode(), commitCount, message);
	        } catch (Exception t) {
	        	t.printStackTrace();
	            logger.error("Failed  to update job status {}", jobId, t);
	        } finally {
	            clearContext();
	            this.jobExecutionSecurityContextListener.removeFromJobExecutionContext(jobExecution);
	        }
	    }


	    private void restoreContext(JobExecution jobExecution) {
	        if (jobExecution.getExecutionContext().containsKey(SECURITY_PARAM_NAME)) {
	            logger.debug("Restore the security context");
	            Authentication authentication = (Authentication) jobExecution.getExecutionContext()
	                    .get(SECURITY_PARAM_NAME);
	            SecurityContext securityContext = SecurityContextHolder.getContext();
	            ORIGINAL_CONTEXT.set(securityContext.getAuthentication());
	            securityContext.setAuthentication(authentication);
	        } else {
	            logger.error("Could not find key {} in the job execution context", SECURITY_PARAM_NAME);
	        }
	    }

	    private void clearContext() {
	        logger.debug("Clear the security context");
	        SecurityContextHolder.clearContext();
	        Authentication originalAuth = ORIGINAL_CONTEXT.get();
	        if (originalAuth != null) {
	            SecurityContextHolder.getContext().setAuthentication(originalAuth);
	            ORIGINAL_CONTEXT.remove();
	        }
	    }
}
