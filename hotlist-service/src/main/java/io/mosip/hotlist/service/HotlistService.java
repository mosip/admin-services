package io.mosip.hotlist.service;

import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.exception.HotlistAppException;

public interface HotlistService {

	public HotlistRequestResponseDTO block(HotlistRequestResponseDTO blockRequest) throws HotlistAppException;

	public HotlistRequestResponseDTO retrieveHotlist(String id, String idType) throws HotlistAppException;

	public HotlistRequestResponseDTO updateHotlist(HotlistRequestResponseDTO blockRequest) throws HotlistAppException;
}
