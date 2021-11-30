package io.mosip.kernel.masterdata.utils;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import io.mosip.kernel.core.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.MachineErrorCode;
import io.mosip.kernel.masterdata.constant.MachineSpecificationErrorCode;
import io.mosip.kernel.masterdata.constant.MachineTypeErrorCode;
import io.mosip.kernel.masterdata.constant.RegistrationCenterErrorCode;
import io.mosip.kernel.masterdata.entity.MachineSpecification;
import io.mosip.kernel.masterdata.entity.MachineType;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.MachineSpecificationRepository;
import io.mosip.kernel.masterdata.repository.MachineTypeRepository;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import tss.tpm.TPMT_PUBLIC;

@Component
public class MachineUtil {

	private static final Logger logger = LoggerFactory.getLogger(MachineUtil.class);

	private static final String ALGORITHM = "RSA";

	@Autowired
	private MachineTypeRepository machineTypeRepository;

	@Autowired
	private MachineSpecificationRepository machineSpecificationRepository;

	@Autowired
	private RegistrationCenterRepository centerRepository;

	@Autowired
	private LanguageUtils languageUtils;
	
	public static String PUBLIC_KEY="PUBLIC";
	public static String SIGN_PUBLIC_KEY="SIGN_PUBLIC";

	public List<MachineSpecification> getMachineSpec() {
		try {
			return machineSpecificationRepository.findAllMachineSpecByIsActiveAndIsDeletedIsNullOrFalse();
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(
					MachineSpecificationErrorCode.MACHINE_SPECIFICATION_FETCH_EXCEPTION.getErrorCode(),
					MachineSpecificationErrorCode.MACHINE_SPECIFICATION_FETCH_EXCEPTION.getErrorMessage());
		}
	}

	public List<MachineType> getMachineTypes() {
		try {
			return machineTypeRepository.findAllMachineTypeByIsActiveAndIsDeletedFalseOrNull();

		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(MachineTypeErrorCode.MACHINE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					MachineTypeErrorCode.MACHINE_TYPE_FETCH_EXCEPTION.getErrorMessage());
		}
	}

	public List<RegistrationCenter> getAllRegistrationCenters(String langCode) {
		try {
			return centerRepository.findAllByIsDeletedFalseOrIsDeletedIsNullAndLangCode(langCode == null ?
					languageUtils.getDefaultLanguage() : langCode);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_FETCH_EXCEPTION.getErrorCode(),
					RegistrationCenterErrorCode.REGISTRATION_CENTER_FETCH_EXCEPTION.getErrorMessage());
		}
	}

	public String getX509EncodedPublicKey(String encodedKey, String key) {
		try {
			try {
				TPMT_PUBLIC tpmPublic = TPMT_PUBLIC.fromTpm(decodeBase64Data(encodedKey));
				return CryptoUtil.encodeToURLSafeBase64(tpmPublic.toTpm());
			} catch (Throwable throwable) {
				logger.error(
						"Failed to parse TPM public key. Using java.security.KeyFactory, Considering it as NON-TPM key");
				X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeBase64Data(encodedKey));
				KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
				PublicKey publicKey = kf.generatePublic(keySpec);
				return CryptoUtil.encodeToURLSafeBase64(publicKey.getEncoded());
			}
		} catch (Exception e) {
			logger.error("Invalid sign public key provided", e);
		}
		if (key.equalsIgnoreCase(PUBLIC_KEY))
			throw new RequestException(MachineErrorCode.INVALID_PUBLIC_KEY.getErrorCode(),
					MachineErrorCode.INVALID_PUBLIC_KEY.getErrorMessage());
		else
			throw new RequestException(MachineErrorCode.INVALID_SIGN_PUBLIC_KEY.getErrorCode(),
					MachineErrorCode.INVALID_SIGN_PUBLIC_KEY.getErrorMessage());
	}

	public byte[] decodeBase64Data(String anyBase64EncodedData){
		try{
			return CryptoUtil.decodeURLSafeBase64(anyBase64EncodedData);
		} catch(IllegalArgumentException argException) {
			logger.error("Error Decoding Base64 URL Safe data, trying with Base64 normal decode.");
		}
		return CryptoUtil.decodeBase64(anyBase64EncodedData);
	}
}
