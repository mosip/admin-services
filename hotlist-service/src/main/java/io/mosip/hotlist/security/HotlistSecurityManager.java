package io.mosip.hotlist.security;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.mosip.hotlist.builder.RestRequestBuilder;
import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.constant.RestServicesConstants;
import io.mosip.hotlist.dto.CryptomanagerRequestDto;
import io.mosip.hotlist.dto.RestRequestDTO;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.exception.HotlistAppUncheckedException;
import io.mosip.hotlist.exception.RestServiceException;
import io.mosip.hotlist.helper.RestHelper;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.util.HMACUtils2;

@Component
public class HotlistSecurityManager {

	private static final String DATA = "data";

	@Autowired
	private RestRequestBuilder restBuilder;

	@Autowired
	private RestHelper restHelper;

	@Value("${mosip.hotlist.crypto.app-id}")
	public String appId;

	public static String getUser() {
		if (Objects.nonNull(SecurityContextHolder.getContext())
				&& Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())
				&& Objects.nonNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
			return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		} else {
			return "";
		}
	}

	public static String hash(final byte[] data) {
		try {
			return HMACUtils2.digestAsPlainText(data);
		} catch (NoSuchAlgorithmException e) {
			// TODO to be removed
			throw new HotlistAppUncheckedException(HotlistErrorConstants.UNKNOWN_ERROR, e);
		}
	}

	public String encrypt(String dataToEncrypt) throws HotlistAppException {
		RequestWrapper<CryptomanagerRequestDto> requestWrapper = new RequestWrapper<>();
		CryptomanagerRequestDto request = new CryptomanagerRequestDto(appId, null, DateUtils.getUTCCurrentDateTime(),
				dataToEncrypt, null, null, true);
		requestWrapper.setRequest(request);
		return encryptDecryptData(restBuilder.buildRequest(RestServicesConstants.CRYPTO_MANAGER_ENCRYPT, requestWrapper,
				ResponseWrapper.class));
	}

	public String decrypt(String dataToDecrypt) throws HotlistAppException {
		RequestWrapper<CryptomanagerRequestDto> requestWrapper = new RequestWrapper<>();
		CryptomanagerRequestDto request = new CryptomanagerRequestDto(appId, null, DateUtils.getUTCCurrentDateTime(),
				dataToDecrypt, null, null, true);
		requestWrapper.setRequest(request);
		return encryptDecryptData(restBuilder.buildRequest(RestServicesConstants.CRYPTO_MANAGER_DECRYPT, requestWrapper,
				ResponseWrapper.class));
	}

	private String encryptDecryptData(final RestRequestDTO restRequest) throws HotlistAppException {
		try {
			ResponseWrapper<Map<String, String>> response = restHelper.requestSync(restRequest);

			if (Objects.nonNull(response) && Objects.nonNull(response.getResponse())
					&& response.getResponse().containsKey(DATA)) {
				return response.getResponse().get(DATA);
			} else {
//				mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO_SECURITY_MANAGER, ENCRYPT_DECRYPT_DATA,
//						"No data block found in response");
				throw new HotlistAppException(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED);
			}
		} catch (RestServiceException e) {
//			mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO_SECURITY_MANAGER, ENCRYPT_DECRYPT_DATA,
//					e.getErrorText());
			throw new HotlistAppException(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED, e);
		}
	}
}
