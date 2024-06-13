package io.mosip.admin.bulkdataupload.batch;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;

public class CustomChunkListener implements ChunkListener {

    private BulkUploadTranscationRepository bulkUploadTranscationRepository;
    
    
	public CustomChunkListener(BulkUploadTranscationRepository bulkUploadTranscationRepository) {
		this.bulkUploadTranscationRepository = bulkUploadTranscationRepository;
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		
	}

	@Override
	public void afterChunk(ChunkContext context) {
		String jobId = (String) context.getStepContext().getJobParameters().get("transactionId");
		long count =  context.getStepContext().getStepExecution().getWriteCount();
      bulkUploadTranscationRepository.updateBulkUploadTransactionCount(jobId, Math.toIntExact(count));
		
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		
	}

}
