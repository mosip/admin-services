package io.mosip.admin.service;

import io.mosip.admin.dto.LostRidDetailsDto;
import io.mosip.admin.dto.LostRidResponseDto;
import io.mosip.admin.dto.SearchInfo;

public interface AdminService {


	LostRidResponseDto lostRid(SearchInfo searchInfo);

    LostRidDetailsDto lostRidDetails(String rid);
}
