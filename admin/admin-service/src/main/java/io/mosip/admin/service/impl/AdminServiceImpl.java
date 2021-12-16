package io.mosip.admin.service.impl;

import java.util.List;

import io.mosip.admin.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.admin.constant.LostRidErrorCode;
import io.mosip.admin.packetstatusupdater.constant.ApiName;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.RestClient;
import io.mosip.admin.service.AdminService;
import io.mosip.kernel.core.util.DateUtils;


@Service
public class AdminServiceImpl implements AdminService {

	@Value("${mosip.registration.processor.lostrid.version:mosip.registration.processor.workflow.search}")
	private String lostRidReqVersion;

	@Value("${mosip.registration.processor.lostrid.id:mosip.registration.lostrid}")
	private String lostRidRequestId;

	@Autowired
	RestClient restClient;

	@Autowired
	ObjectMapper objectMapper;


	@Override
	public LostRidResponseDto lostRid(SearchInfo searchInfo) {
		LostRidResponseDto lostRidResponseDto = new LostRidResponseDto();
		RegProcRequestWrapper<SearchInfo> procRequestWrapper = new RegProcRequestWrapper<>();
		createLostRidRequest(searchInfo);
		procRequestWrapper.setId(lostRidRequestId);
		procRequestWrapper.setVersion(lostRidReqVersion);
		procRequestWrapper.setRequest(searchInfo);
		String dateTime = DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime());
		procRequestWrapper.setRequesttime(dateTime);
		try {
			String response = restClient.postApi(ApiName.LOST_RID_API, null, "", "", MediaType.APPLICATION_JSON,
					procRequestWrapper, String.class);
			lostRidResponseDto = objectMapper.readValue(response, LostRidResponseDto.class);
		} catch (Exception e) {
			throw new RequestException(LostRidErrorCode.UNABLE_TO_RETRIEVE_LOSTRID.getErrorCode(),
					LostRidErrorCode.UNABLE_TO_RETRIEVE_LOSTRID.getErrorMessage()
							+ e);
		}
		return lostRidResponseDto;
	}

	private void createLostRidRequest(SearchInfo searchInfoRequest) {
		for(FilterInfo fi:searchInfoRequest.getFilters()){
			if(fi.getType().equalsIgnoreCase("contains")) {
				fi.setType("equals");
			}
			if(fi.getColumnName().equalsIgnoreCase("locationCode")) {
				fi.setColumnName("postalCode");
			}
		}
		if (searchInfoRequest.getSort().isEmpty()) {
			List<SortInfo> sortInfos = searchInfoRequest.getSort();
			SortInfo sortInfo = new SortInfo();
			sortInfo.setSortField("registrationDate");
			sortInfo.setSortType("desc");
			sortInfos.add(sortInfo);
			searchInfoRequest.setSort(sortInfos);
		}

	}


}
