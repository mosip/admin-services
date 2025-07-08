package io.mosip.admin.bulkdataupload.batch;

import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.file.FlatFileParseException;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

public class JobResultListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobResultListener.class);
    private static String STATUS_MESSAGE = " <br/> STATUS: %s, MESSAGE: %s";
    private AuditUtil auditUtil;
    private BulkUploadTranscationRepository bulkUploadTranscationRepository;

    public JobResultListener(BulkUploadTranscationRepository bulkUploadTranscationRepository,
                             AuditUtil auditUtil) {
        this.bulkUploadTranscationRepository = bulkUploadTranscationRepository;
        this.auditUtil = auditUtil;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // TODO Nothing
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobId = jobExecution.getJobParameters().getString("transactionId");
        logger.info("Job completed : {}", jobId);
        try {
            List<String> failures = new ArrayList<String>();
            jobExecution.getStepExecutions().forEach(step -> {
                step.getFailureExceptions().forEach(failure -> {
                	if (failure instanceof FlatFileParseException && failure.getCause() instanceof ConstraintViolationException) {
                        failures.add("Line --> " + ((FlatFileParseException) failure).getLineNumber() +  " --> "+
                                ((FlatFileParseException) failure).getCause().getMessage());
                    }
                    else if(failure instanceof FlatFileParseException){
                        failures.add("Line --> " + ((FlatFileParseException) failure).getLineNumber() +
                                " --> Datatype mismatch/ Validation error / Failed to write into object");
                    } else
                        failures.add(failure.getCause() != null ? failure.getCause().getMessage() : failure.getMessage());
                });
            });

            String message = String.format(STATUS_MESSAGE,
                    jobExecution.getStatus().toString(), failures.isEmpty() ? "0 Errors" : failures.toString());
            auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_COMPLETED,
                            jobId + " --> " + message), jobExecution.getJobParameters().getString("username"));

            bulkUploadTranscationRepository.updateBulkUploadTransactionStatus(jobId, jobExecution.getExitStatus().getExitCode(), message);

        } catch (Throwable t) {
            logger.error("Failed  to update job status {}", jobId, t);
        }
    }
}
