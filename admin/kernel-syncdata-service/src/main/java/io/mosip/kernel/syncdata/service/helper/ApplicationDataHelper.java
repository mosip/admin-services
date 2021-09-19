package io.mosip.kernel.syncdata.service.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import io.mosip.kernel.syncdata.dto.AppAuthenticationMethodDto;
import io.mosip.kernel.syncdata.dto.AppDetailDto;
import io.mosip.kernel.syncdata.dto.AppRolePriorityDto;
import io.mosip.kernel.syncdata.dto.ApplicationDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.AppAuthenticationMethod;
import io.mosip.kernel.syncdata.entity.AppDetail;
import io.mosip.kernel.syncdata.entity.AppRolePriority;
import io.mosip.kernel.syncdata.entity.Application;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

public class ApplicationDataHelper {
		
	CompletableFuture<List<ApplicationDto>> applications = null;	
	CompletableFuture<List<AppAuthenticationMethodDto>> appAuthenticationMethods = null;
	CompletableFuture<List<AppDetailDto>> appDetails = null;
	CompletableFuture<List<AppRolePriorityDto>> appRolePriorities = null;
	
	private LocalDateTime lastUpdated;
	private LocalDateTime currentTimestamp;
	private String publicKey;
	
	public ApplicationDataHelper(LocalDateTime lastUpdated, LocalDateTime currentTimestamp, String publicKey) {
		this.lastUpdated = lastUpdated;
		this.currentTimestamp = currentTimestamp;
		this.publicKey = publicKey;
	}

	public void retrieveData(final SyncMasterDataServiceHelper serviceHelper, final List<CompletableFuture> futures) {
		this.appAuthenticationMethods = serviceHelper.getAppAuthenticationMethodDetails(this.lastUpdated, this.currentTimestamp);
		this.appRolePriorities = serviceHelper.getAppRolePriorityDetails(this.lastUpdated, this.currentTimestamp);
		
		futures.add(this.appAuthenticationMethods);
		futures.add(this.appRolePriorities);
	}	
	
	public void fillRetrievedData(final SyncMasterDataServiceHelper serviceHelper, final List<SyncDataBaseDto> list) 
			throws InterruptedException, ExecutionException {
		serviceHelper.getSyncDataBaseDto(AppAuthenticationMethod.class, "structured", this.appAuthenticationMethods.get(), this.publicKey, list);
		serviceHelper.getSyncDataBaseDto(AppRolePriority.class, "structured", this.appRolePriorities.get(), this.publicKey, list);
	}
}
