package io.mosip.admin.bulkdataupload.batch;

import java.sql.Types;
import javax.sql.DataSource;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class CustomChunkListener implements ChunkListener {

	private static String UPDATE_QUERY = "UPDATE bulkupload_transaction SET record_count=record_count+? , upd_dtimes=now() WHERE id=?";
    private DataSource dataSource;
    
    
    
	public CustomChunkListener(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterChunk(ChunkContext context) {
		// TODO Auto-generated method stub
		String jobId = (String) context.getStepContext().getJobParameters().get("transactionId");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
      Object[] params = { context.getStepContext().getStepExecution().getWriteCount(), jobId};
      int[] types = {Types.INTEGER, Types.VARCHAR};
      jdbcTemplate.update(UPDATE_QUERY, params, types);
		
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		// TODO Auto-generated method stub
		
	}

}
