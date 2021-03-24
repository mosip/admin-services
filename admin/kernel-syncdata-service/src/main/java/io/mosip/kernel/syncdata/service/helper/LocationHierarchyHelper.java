package io.mosip.kernel.syncdata.service.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.dto.LocationHierarchyDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.LocationHierarchy;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

public class LocationHierarchyHelper {

	CompletableFuture<List<LocationHierarchyDto>> locationHierarchyLevelDtos = null;
	CompletableFuture<List<DynamicFieldDto>> dynamicFieldDtos = null;

	private String publicKey;
	private LocalDateTime lastUpdated;

	public LocationHierarchyHelper(LocalDateTime lastUpdated, String publicKey) {
		this.lastUpdated = lastUpdated;
		this.publicKey = publicKey;
	}

	public void retrieveData(final SyncMasterDataServiceHelper serviceHelper, final List<CompletableFuture> futures) {
		this.locationHierarchyLevelDtos = serviceHelper.getLocationHierarchyList(this.lastUpdated);
		this.dynamicFieldDtos = serviceHelper.getAllDynamicFields(this.lastUpdated);

		futures.add(this.locationHierarchyLevelDtos);
		futures.add(this.dynamicFieldDtos);
	}

	public void fillRetrievedData(final SyncMasterDataServiceHelper serviceHelper, final List<SyncDataBaseDto> list)
			throws InterruptedException, ExecutionException {
		serviceHelper.getSyncDataBaseDto(LocationHierarchy.class, "structured", this.locationHierarchyLevelDtos.get(),
				this.publicKey, list);

		//Fills dynamic field data
		Map<String, List<DynamicFieldDto>> data = new HashMap<String, List<DynamicFieldDto>>();
		this.dynamicFieldDtos.get().forEach(dto -> {
			if(!data.containsKey(dto.getName())) {
				List<DynamicFieldDto> langBasedData = new ArrayList<DynamicFieldDto>();
				langBasedData.add(dto);
				data.put(dto.getName(), langBasedData);
			}
			else
				data.get(dto.getName()).add(dto);
		});

		for(String key : data.keySet()) {
			serviceHelper.getSyncDataBaseDto(key, "dynamic", data.get(key), publicKey, list);
		}
	}
}
