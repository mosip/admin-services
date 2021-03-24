package io.mosip.kernel.syncdata.service.helper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.mosip.kernel.syncdata.dto.LocationHierarchyDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.LocationHierarchy;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

public class LocationHierarchyHelper {

	CompletableFuture<List<LocationHierarchyDto>> locationHierarchyLevelDtos = null;

	private String publicKey;
	private LocalDateTime lastUpdated;
	private LocalDateTime currentTimestamp;

	public LocationHierarchyHelper(LocalDateTime lastUpdated, LocalDateTime currentTimestamp, String publicKey) {
		this.lastUpdated = lastUpdated;
		this.currentTimestamp = currentTimestamp;
		this.publicKey = publicKey;
	}

	public void retrieveData(final SyncMasterDataServiceHelper serviceHelper, final List<CompletableFuture> futures) {
		// this.applications = serviceHelper.getApplications(this.lastUpdated,
		// this.currentTimestamp);
		this.locationHierarchyLevelDtos = serviceHelper.getLocationHierarchyList(this.lastUpdated, this.currentTimestamp);

		// futures.add(this.applications);
		futures.add(this.locationHierarchyLevelDtos);

	}

	public void fillRetrievedData(final SyncMasterDataServiceHelper serviceHelper, final List<SyncDataBaseDto> list)
			throws InterruptedException, ExecutionException {
		// list.add(serviceHelper.getSyncDataBaseDto(Application.class, "structured",
		// this.applications.get(), this.publicKey));
		serviceHelper.getSyncDataBaseDto(LocationHierarchyDto.class, "structured", this.locationHierarchyLevelDtos.get(),
				this.publicKey, list);
	}

}
