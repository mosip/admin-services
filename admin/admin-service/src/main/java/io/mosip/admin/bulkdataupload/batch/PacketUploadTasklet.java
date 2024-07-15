package io.mosip.admin.bulkdataupload.batch;

import io.mosip.admin.bulkdataupload.dto.PacketUploadStatus;
import io.mosip.admin.bulkdataupload.service.PacketUploadService;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class PacketUploadTasklet implements Tasklet, InitializingBean {

    private String fileName;
    private byte[] file;
    private PacketUploadService packetUploadService;
    private String centerId;
    private String supervisorStatus;
    private String source;
    private String process;
    private String mode;

    public PacketUploadTasklet(String fileName, byte[] packet, PacketUploadService packetUploadService,
                              String centerId, String supervisorStatus, String source, String process,
                               String mode) {
        this.fileName = fileName;
        this.file = packet;
        this.packetUploadService = packetUploadService;
        this.centerId = centerId;
        this.source = source;
        this.process = process;
        this.supervisorStatus = supervisorStatus;
        this.mode = mode;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        PacketUploadStatus status = switch (mode) {
            case "UPLOAD" -> packetUploadService.onlyUploadPacket(this.fileName, this.file);
            case "SYNC-UPLOAD" ->
                    packetUploadService.syncAndUploadPacket(this.fileName, this.file, centerId, supervisorStatus, source, process,
                            (String) chunkContext.getStepContext().getJobParameters().get("transactionId"));
            default -> null;
        };

        if(null!=status && status.isFailed())
            throw new JobExecutionException(this.fileName + " --> " + status.getMessage());

        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(file, "Packet file must be set");
        Assert.notNull(packetUploadService, "PacketUploadService must be set");
    }
}
