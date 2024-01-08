package io.mosip.admin.service.impl;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.time.LocalDate;

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

	@Value("${mosip.registration.processor.lostrid.max-registration-date-filter-interval}")
	private String max_reg_date_interval;
	
	@Value("${mosip.registration.processor.lostrid.reg-date-pattern}")
	private String regDatePattern;
	
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
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(regDatePattern);
			List<FilterInfo> filters=searchInfo.getFilters();
			LocalDate dateForm = LocalDate.parse(filters.get(0).getFromValue(), dtf);
			LocalDate dateTo = LocalDate.parse(filters.get(0).getToValue(), dtf);
			long noOfDaysBetween = ChronoUnit.DAYS.between(dateForm, dateTo);
			long maxRegDateInterval = Long.parseLong(max_reg_date_interval);
			if (noOfDaysBetween > maxRegDateInterval) {
				throw new RequestException(LostRidErrorCode.LOST_RID_DATE_RANGE_EXCEEDED.getErrorCode(),
						String.format(LostRidErrorCode.LOST_RID_DATE_RANGE_EXCEEDED.getErrorMessage(),maxRegDateInterval));
			}
			String response = restClient.postApi(ApiName.LOST_RID_API, MediaType.APPLICATION_JSON,
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
