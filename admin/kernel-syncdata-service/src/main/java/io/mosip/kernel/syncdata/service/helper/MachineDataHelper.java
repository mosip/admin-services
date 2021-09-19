package io.mosip.kernel.syncdata.service.helper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.mosip.kernel.syncdata.dto.MachineDto;
import io.mosip.kernel.syncdata.dto.MachineSpecificationDto;
import io.mosip.kernel.syncdata.dto.MachineTypeDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.entity.MachineSpecification;
import io.mosip.kernel.syncdata.entity.MachineType;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

public class MachineDataHelper {
		
	private String regCenterId;
	private String machineId;
	private LocalDateTime lastUpdated;
	private LocalDateTime currentTimestamp;
	
	private CompletableFuture<List<MachineDto>> machineDetails = null;
	private CompletableFuture<List<MachineSpecificationDto>> machineSpecification = null;
	private CompletableFuture<List<MachineTypeDto>> machineType = null;

	private String publicKey;
	
	public MachineDataHelper(String regCenterId, String machineId, LocalDateTime lastUpdated, LocalDateTime currentTimestamp, String publicKey) {
		this.regCenterId = regCenterId;
		this.lastUpdated = lastUpdated;
		this.currentTimestamp = currentTimestamp;
		this.publicKey = publicKey;
		this.machineId = machineId;
	}
	
	public void retrieveData(final SyncMasterDataServiceHelper serviceHelper, final List<CompletableFuture> futures) {
		this.machineDetails = serviceHelper.getMachines(this.regCenterId, this.lastUpdated, this.currentTimestamp, this.machineId);
		futures.add(this.machineDetails);
	}
	
	public void fillRetrievedData(final SyncMasterDataServiceHelper serviceHelper, final List<SyncDataBaseDto> list) 
			throws InterruptedException, ExecutionException {
		serviceHelper.getSyncDataBaseDto(Machine.class, "structured", this.machineDetails.get(), this.publicKey, list);
	}

}
