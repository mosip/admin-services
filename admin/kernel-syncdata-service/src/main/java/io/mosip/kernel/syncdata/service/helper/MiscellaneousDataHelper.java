package io.mosip.kernel.syncdata.service.helper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

public class MiscellaneousDataHelper {
	
	private String machineId;
	private LocalDateTime lastUpdated;
	private LocalDateTime currentTimestamp;
	
	CompletableFuture<List<HolidayDto>> holidays = null;
	CompletableFuture<List<BlacklistedWordsDto>> blacklistedWords = null;
	CompletableFuture<List<ScreenAuthorizationDto>> screenAuthorizations = null;
	CompletableFuture<List<ScreenDetailDto>> screenDetails = null;
	CompletableFuture<List<ProcessListDto>> processList = null;
	CompletableFuture<List<SyncJobDefDto>> syncJobDefDtos = null;
	CompletableFuture<List<PermittedConfigDto>> permittedConfigDtos = null;

	private String publicKey;
	
	public MiscellaneousDataHelper(String machineId, LocalDateTime lastUpdated, LocalDateTime currentTimestamp, String publicKey) {
		this.machineId = machineId;
		this.lastUpdated = lastUpdated;
		this.currentTimestamp = currentTimestamp;
		this.publicKey = publicKey;
	}
	
	public void retrieveData(final SyncMasterDataServiceHelper serviceHelper, final List<CompletableFuture> futures) {
		this.holidays = serviceHelper.getHolidays(this.lastUpdated, this.machineId, this.currentTimestamp);
		this.blacklistedWords = serviceHelper.getBlackListedWords(this.lastUpdated, this.currentTimestamp);
	
		this.screenAuthorizations = serviceHelper.getScreenAuthorizationDetails(this.lastUpdated, this.currentTimestamp);
		this.screenDetails = serviceHelper.getScreenDetails(this.lastUpdated, this.currentTimestamp);
		
		this.processList = serviceHelper.getProcessList(this.lastUpdated, this.currentTimestamp);		

		this.syncJobDefDtos = serviceHelper.getSyncJobDefDetails(this.lastUpdated, this.currentTimestamp);

		this.permittedConfigDtos = serviceHelper.getPermittedConfig(this.lastUpdated, this.currentTimestamp);
		
		futures.add(this.holidays);
		futures.add(this.blacklistedWords);
		
		futures.add(this.screenAuthorizations);
		futures.add(this.screenDetails);
		
		futures.add(this.processList);

		futures.add(this.syncJobDefDtos);

		futures.add(this.permittedConfigDtos);
	}
	
	public void fillRetrievedData(final SyncMasterDataServiceHelper serviceHelper, final List<SyncDataBaseDto> list) 
			throws InterruptedException, ExecutionException {
		serviceHelper.getSyncDataBaseDto(Holiday.class, "structured", this.holidays.get(), this.publicKey, list);
		serviceHelper.getSyncDataBaseDto(BlacklistedWords.class, "structured", this.blacklistedWords.get(), this.publicKey,list);
		serviceHelper.getSyncDataBaseDto(ScreenAuthorization.class, "structured", this.screenAuthorizations.get(), this.publicKey,list);
		serviceHelper.getSyncDataBaseDto(ScreenDetail.class, "structured", this.screenDetails.get(), this.publicKey,list);
		serviceHelper.getSyncDataBaseDto(ProcessList.class, "structured", this.processList.get(), this.publicKey, list);
		serviceHelper.getSyncDataBaseDto(SyncJobDef.class, "structured", this.syncJobDefDtos.get(), this.publicKey, list);
		serviceHelper.getSyncDataBaseDto(PermittedLocalConfig.class, "structured", this.permittedConfigDtos.get(), this.publicKey, list);
	}
}
