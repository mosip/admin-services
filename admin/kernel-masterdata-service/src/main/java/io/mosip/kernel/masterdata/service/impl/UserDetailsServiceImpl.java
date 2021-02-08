package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.mosip.kernel.core.authmanager.model.UserDetailsResponseDto;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.UserDetailsErrorCode;
import io.mosip.kernel.masterdata.dto.PageDto;
import io.mosip.kernel.masterdata.dto.UserDetailsDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserDetailsExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.UserDetails;
import io.mosip.kernel.masterdata.entity.UserDetailsHistory;
import io.mosip.kernel.masterdata.entity.id.IdAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.UserDetailsHistoryRepository;
import io.mosip.kernel.masterdata.repository.UserDetailsRepository;
import io.mosip.kernel.masterdata.service.RegistrationCenterService;
import io.mosip.kernel.masterdata.service.UserDetailsHistoryService;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	UserDetailsHistoryRepository history;

	@Autowired
	UserDetailsHistoryService userDetailsHistoryService;

	@Autowired
	MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	RegistrationCenterService registrationCenterService;

	@Autowired
	private AuditUtil auditUtil;

	@Override
	public UserDetailsDto getUser(String id) {
		UserDetails ud = userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(id);
		return getDto(ud);
	}

	@Override
	public PageDto<UserDetailsExtnDto> getUsers(int pageNumber, int pageSize, String sortBy, String direction) {
		List<UserDetailsExtnDto> userDetails = null;
		PageDto<UserDetailsExtnDto> pageDto = null;
		try {
			Page<UserDetails> pageData = userDetailsRepository
					.findAllByIsDeletedFalseorIsDeletedIsNull(PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(direction), sortBy)));
			if (pageData != null && pageData.getContent() != null && !pageData.getContent().isEmpty()) {
				userDetails = MapperUtils.mapAll(pageData.getContent(), UserDetailsExtnDto.class);
				pageDto = new PageDto<>(pageData.getNumber(), pageSize, pageData.getSort(), pageData.getTotalElements(), pageData.getTotalPages(), userDetails);
			} else {
				throw new DataNotFoundException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
						UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
			}
		} catch (DataAccessException | DataAccessLayerException exception) {
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorCode(),
			UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}
		return pageDto;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IdResponseDto deleteUser(String id) {
		IdResponseDto idResponse = new IdResponseDto();
		try {
			Optional<UserDetails> ud = userDetailsRepository.findById(id);
			ud.ifPresent(user -> {
				userDetailsRepository.delete(user);
				UserDetailsHistory udh = new UserDetailsHistory();
				MapperUtils.map(user, udh);
				MapperUtils.setBaseFieldValue(user, udh);
				udh.setIsActive(false);
				udh.setIsDeleted(true);
				udh.setCreatedBy(MetaDataUtils.getContextUser());
				udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
				userDetailsHistoryService.createUserDetailsHistory(udh);
				MapperUtils.map(user, idResponse);
			} );
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException 
		 | SecurityException e) {
			auditUtil.auditRequest(
			String.format(MasterDataConstant.CREATE_ERROR_AUDIT, UserDetails.class.getSimpleName()),
			MasterDataConstant.AUDIT_SYSTEM,
			String.format(MasterDataConstant.FAILURE_DESC,
					UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
			UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		auditUtil.auditRequest(String.format(MasterDataConstant.DECOMMISSION_SUCCESS, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.DECOMMISSION_SUCCESS,
				 idResponse.getId()));
		return idResponse;
		
	}

	@Override
	public List<UserDetailsExtnDto> getUsersByRegistrationCenter(String regCenterId, int pageNumber, int pageSize, String sortBy, String orderBy) {
				List<UserDetailsExtnDto> userDetails = null;
				PageDto<UserDetailsExtnDto> pageDto = null;
				try {
					List<UserDetails> pageData = userDetailsRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(regCenterId, PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));
					if (pageData != null && !pageData.isEmpty()) {
						userDetails = MapperUtils.mapAll(pageData, UserDetailsExtnDto.class);
					} else {
						throw new DataNotFoundException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
								UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
					}
				} catch (DataAccessException | DataAccessLayerException exception) {
					throw new MasterDataServiceException(UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorMessage()
									+ ExceptionUtils.parseException(exception));
				}
			return userDetails;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IdAndLanguageCodeID createUser(UserDetailsDto userDetailsDto) {
		UserDetails ud;
		try {
			userDetailsDto = masterdataCreationUtil.createMasterData(UserDetails.class, userDetailsDto);
			ud = MetaDataUtils.setCreateMetaData(userDetailsDto, UserDetails.class);
			registrationCenterService.getRegistrationCentersByID(userDetailsDto.getRegCenterId()); //Throws exception if not found
			userDetailsRepository.create(ud);
			UserDetailsHistory udh = new UserDetailsHistory();
				MapperUtils.map(ud, udh);
				MapperUtils.setBaseFieldValue(ud, udh);
				udh.setIsActive(true);
				udh.setIsDeleted(false);
				udh.setCreatedBy(MetaDataUtils.getContextUser());
				udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
				userDetailsHistoryService.createUserDetailsHistory(udh);
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.CREATE_ERROR_AUDIT, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
			UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}

		IdAndLanguageCodeID idAndLanguageCodeID = new IdAndLanguageCodeID();
		MapperUtils.map(ud, idAndLanguageCodeID);
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
				UserDetails.class.getSimpleName(), idAndLanguageCodeID.getId()));
		return idAndLanguageCodeID;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserDetailsDto updateUser(UserDetailsDto userDetailsDto) {
		UserDetails ud;
		try {
			userDetailsDto = masterdataCreationUtil.updateMasterData(UserDetails.class, userDetailsDto);
			ud = MetaDataUtils.setCreateMetaData(userDetailsDto, UserDetails.class);
			userDetailsRepository.update(ud);
			UserDetailsHistory udh = new UserDetailsHistory();
			MapperUtils.map(ud, udh);
			MapperUtils.setBaseFieldValue(ud, udh);
			udh.setIsActive(true);
			udh.setIsDeleted(false);
			udh.setCreatedBy(MetaDataUtils.getContextUser());
			udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
			userDetailsHistoryService.createUserDetailsHistory(udh);
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.CREATE_ERROR_AUDIT, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
			UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));			
		}

		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_UPDATE, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
				UserDetails.class.getSimpleName(), ud.getId()));
		return userDetailsDto;
	}

	
	private UserDetailsDto getDto(UserDetails ud){
		UserDetailsDto udDto = new UserDetailsDto();
		udDto.setId(ud.getId());
		udDto.setRegCenterId(ud.getRegCenterId());
		udDto.setIsActive(ud.getIsActive());
		udDto.setLangCode(ud.getLangCode());
		return udDto;
	}
}
