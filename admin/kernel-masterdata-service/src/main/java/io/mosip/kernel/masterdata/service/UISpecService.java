package io.mosip.kernel.masterdata.service;

import java.util.List;

import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecPublishDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecResponseDto;

/**
 * 
 * @author Nagarjuna
 *
 */

public interface UISpecService {

	static final String STATUS_DRAFT = "DRAFT";
	static final String STATUS_PUBLISHED = "PUBLISHED";

	/**
	 * Fetch active and latest versioned ui spec based on domain
	 * 
	 * @returnge
	 */
	public List<UISpecResponseDto> getLatestUISpec(String domain);
	
	/**
	 * 
	 * @param identitySchemaId
	 * @param domain
	 * @return
	 */
	public List<UISpecResponseDto> getLatestUISpec(String identitySchemaId,String domain);

	/**
	 * Fetch active ui spec based on version and domain
	 * 
	 * @return
	 */
	public List<UISpecResponseDto> getUISpec(double version,String domain);
	
	/**
	 * 
	 * @param idVersion
	 * @param domain
	 * @param type
	 * @return
	 */
	public List<UISpecResponseDto> getUISpec(double version,String domain,String type);
	
	/**
	 * 
	 * @param domain
	 * @param type
	 * @return
	 */
	public List<UISpecResponseDto> getUISpec(String domain,String type);
	
	/**
	 * 
	 * @param identitySchemaId
	 * @param domain
	 * @param type
	 * @return
	 */
	public List<UISpecResponseDto> getUISpec(String identitySchemaId,String domain, String type);

	/**
	 * Fetches all active ui spec's both in DRAFT and PUBLISHED status
	 * 
	 * @return
	 */
	public PageDto<UISpecResponseDto> getAllUISpecs(int pageNumber, int pageSize, String sortBy, String orderBy);

	/**
	 * Create new ui spec in DRAFT status
	 * 
	 * @param dto
	 * @return
	 */
	public UISpecResponseDto defineUISpec(UISpecDto dto);

	/**
	 * update ui spec and its status
	 * 
	 * @param dto
	 * @return
	 */
	public UISpecResponseDto updateUISpec(String id, UISpecDto dto);

	/**
	 * update ui spec in DRAFT status to PUBLISHED status and also increment
	 * version by 0.1
	 * 
	 * @param dto
	 * @return
	 */
	public String publishUISpec(UISpecPublishDto dto);

	/**
	 * update only is_deleted flag of ui pec in DRAFT status it is not
	 * allowed to delete PUBLISHED ui spec
	 * 
	 * @param id
	 * @return
	 */
	public String deleteUISpec(String id);	

	/**
	 * 
	 * @param domain
	 * @param version
	 * @param type
	 * @param identitySchemaVersion
	 * @return
	 */
	public List<UISpecResponseDto> getLatestPublishedUISpec(String domain, double version, String type,
			double identitySchemaVersion);

}
