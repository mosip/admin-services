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
import org.springframework.web.multipart.MultipartFile;

public class PacketUploadTasklet implements Tasklet, InitializingBean {

    private MultipartFile file;
    private PacketUploadService packetUploadService;
    private String centerId;
    private String supervisorStatus;
    private String source;
    private String process;
    private String mode;

    public PacketUploadTasklet(MultipartFile multipartFile, PacketUploadService packetUploadService,
                              String centerId, String supervisorStatus, String source, String process,
                               String mode) {
        this.file = multipartFile;
        this.packetUploadService = packetUploadService;
        this.centerId = centerId;
        this.source = source;
        this.process = process;
        this.supervisorStatus = supervisorStatus;
        this.mode = mode;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        PacketUploadStatus status = null;
        switch (mode) {
            case "UPLOAD" :
                status = packetUploadService.onlyUploadPacket(file);
                break;
            case "SYNC-UPLOAD":
                status = packetUploadService.syncAndUploadPacket(file, centerId, supervisorStatus, source, process,
                        (String) chunkContext.getStepContext().getJobParameters().get("transactionId"));
                break;
        }
        if(status.isFailed())
            throw new JobExecutionException(this.file.getOriginalFilename() + " --> " + status.getMessage());

        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(file, "Packet file must be set");
        Assert.notNull(packetUploadService, "PacketUploadService must be set");
    }
}
