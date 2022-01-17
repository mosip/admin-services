package io.mosip.admin.bulkdataupload.batch;

import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobResultListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobResultListener.class);
    private static String STATUS_MESSAGE = " <br/> READ: %d, STATUS: %s, MESSAGE: %s";
    private static String UPDATE_QUERY = "UPDATE bulkupload_transaction SET status_code=?, record_count=record_count+?, upload_description=upload_description||? , upd_dtimes=now() WHERE id=?";
    private DataSource dataSource;
    private AuditUtil auditUtil;

    public JobResultListener(DataSource dataSource,
                             AuditUtil auditUtil) {
        this.dataSource = dataSource;
        this.auditUtil = auditUtil;
    }



    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Job started : {}", jobExecution.getJobParameters().getString("transactionId"));
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
                        failures.add(failure.getMessage());
                });
            });

            Optional<Integer> commitResult = jobExecution.getStepExecutions().stream().map(StepExecution::getCommitCount).findFirst();
            Optional<Integer> readResult = jobExecution.getStepExecutions().stream().map(StepExecution::getCommitCount).findFirst();
            int readCount = readResult.isPresent() ? readResult.get() : 0;
            int commitCount = commitResult.isPresent() ? commitResult.get() : 0;
            String message = String.format(STATUS_MESSAGE, readCount,
                    jobExecution.getStatus().toString(), failures.isEmpty() ? "0 Errors" : failures.toString());
            auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_COMPLETED,
                            jobId + " --> " + message), jobExecution.getJobParameters().getString("username"));

            JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
            Object[] params = {jobExecution.getExitStatus().getExitCode(), commitCount, message, jobId};
            int[] types = {Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
            jdbcTemplate.update(UPDATE_QUERY, params, types);

        } catch (Throwable t) {
            logger.error("Failed  to update job status {}", jobId, t);
        }
    }
}
