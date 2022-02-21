package io.mosip.admin.bulkdataupload.batch;

import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import org.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobResultListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobResultListener.class);
    private static String STATUS_MESSAGE = " <br/> STATUS: %s, MESSAGE: %s";
    private static String UPDATE_QUERY = "UPDATE bulkupload_transaction SET status_code=?, upload_description=upload_description||? , upd_dtimes=now() WHERE id=?";
    private DataSource dataSource;
    private AuditUtil auditUtil;
    private JobExecutionSecurityContextListener jobExecutionSecurityContextListener;

    private static final String SECURITY_PARAM_NAME = "security-param";
    private static final ThreadLocal<Authentication> ORIGINAL_CONTEXT = new ThreadLocal<>();

    public JobResultListener(DataSource dataSource,
                             AuditUtil auditUtil, JobExecutionSecurityContextListener jobExecutionSecurityContextListener) {
        this.dataSource = dataSource;
        this.auditUtil = auditUtil;
        this.jobExecutionSecurityContextListener = new JobExecutionSecurityContextListener();
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Job started : {}", jobExecution.getJobParameters().getString("transactionId"));
        this.jobExecutionSecurityContextListener.fillJobExecutionContext(jobExecution);

        if(jobExecution.getStepExecutions().isEmpty()) {
            restoreContext(jobExecution);
        }
        else {
            for(StepExecution stepExecution : jobExecution.getStepExecutions()) {
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

            String message = String.format(STATUS_MESSAGE,
                    jobExecution.getStatus().toString(), failures.isEmpty() ? "0 Errors" : failures.toString());
            auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_COMPLETED,
                            jobId + " --> " + message), jobExecution.getJobParameters().getString("username"));

            JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
            Object[] params = {jobExecution.getExitStatus().getExitCode(), message, jobId};
            int[] types = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
            jdbcTemplate.update(UPDATE_QUERY, params, types);

        } catch (Throwable t) {
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
