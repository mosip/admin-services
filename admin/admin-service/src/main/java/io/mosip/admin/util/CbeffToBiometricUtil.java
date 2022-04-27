package io.mosip.admin.util;

import io.mosip.admin.constant.AdminErrorCode;
import io.mosip.admin.packetstatusupdater.exception.DataNotFoundException;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.spi.CbeffUtil;
import io.mosip.kernel.biometrics.spi.IBioApi;
import io.mosip.kernel.cbeffutil.impl.CbeffImpl;
import io.mosip.kernel.core.bioapi.exception.BiometricException;
import io.mosip.kernel.core.cbeffutil.exception.CbeffException;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.SingleType;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class CbeffToBiometricUtil.
 * 
 * @author M1048358 Alok
 * @author M1030448 Jyoti
 */
public class CbeffToBiometricUtil {

	/** The print logger. */
	private static final Logger logger = LoggerFactory.getLogger(AdminProxyServiceUtil.class);


	/** The cbeffutil. */
	private CbeffUtil cbeffutil = new CbeffImpl();

	/**
	 * Instantiates a new cbeff to biometric util.
	 *
	 * @param cbeffutil the cbeffutil
	 */
/*	public CbeffToBiometricUtil(CbeffUtil cbeffutil) {
		this.cbeffutil = cbeffutil;
	}*/

	/**
	 * Instantiates biometric util
	 * 
	 */
	public CbeffToBiometricUtil() {

	}

	/**
	 * Gets the photo.
	 *
	 * @param cbeffFileString the cbeff file string
	 * @param type            the type
	 * @param subType         the sub type
	 * @return the photo
	 * @throws Exception the exception
	 */
	public byte[] getImageBytes(String cbeffFileString, String type, List<String> subType) {
		logger.debug("CbeffToBiometricUtil::getImageBytes()::entry");
		byte[] photoBytes = null;
		if (cbeffFileString != null) {
			List<BIR> bIRTypeList = null;
			try {
				bIRTypeList = getBIRTypeList(cbeffFileString);
				photoBytes = getPhotoByTypeAndSubType(bIRTypeList, type, subType);
			} catch (Exception e) {
				throw new DataNotFoundException(AdminErrorCode.DATA_NOT_FOUND.getErrorCode(),AdminErrorCode.DATA_NOT_FOUND.getErrorMessage());
			}
		}
		logger.debug("CbeffToBiometricUtil::getImageBytes()::exit");
		return photoBytes;
	}

	/**
	 * Gets the photo by type and sub type.
	 *
	 * @param type        the type
	 * @param subType     the sub type
	 * @return the photo by type and sub type
	 */
	private byte[] getPhotoByTypeAndSubType(List<BIR> bIRList, String type, List<String> subType) {
		byte[] photoBytes = null;
		for (BIR bir : bIRList) {
			if (bir.getBdbInfo() != null) {
				List<BiometricType> singleTypeList = bir.getBdbInfo().getType();
				List<String> subTypeList = bir.getBdbInfo().getSubtype();

				boolean isType = isBiometricType(type, singleTypeList);
				boolean isSubType = isSubType(subType, subTypeList);

				if (isType && isSubType) {
					photoBytes = bir.getBdb();
					break;
				}
			}
		}
		return photoBytes;
	}

	/**
	 * Checks if is sub type.
	 *
	 * @param subType     the sub type
	 * @param subTypeList the sub type list
	 * @return true, if is sub type
	 */
	private boolean isSubType(List<String> subType, List<String> subTypeList) {
		return subTypeList.equals(subType) ? Boolean.TRUE : Boolean.FALSE;
	}

	private boolean isBiometricType(String type, List<BiometricType> biometricTypeList) {
		boolean isType = false;
		for (BiometricType biometricType : biometricTypeList) {
			if (biometricType.value().equalsIgnoreCase(type)) {
				isType = true;
			}
		}
		return isType;
	}

	/**
	 * Gets the BIR type list.
	 *
	 * @param cbeffFileString the cbeff file string
	 * @return the BIR type list
	 * @throws Exception the exception
	 */

	public List<BIR> getBIRTypeList(String cbeffFileString) throws Exception {
		return cbeffutil.getBIRDataFromXML(Base64.decodeBase64(cbeffFileString));
	}

	/**
	 * Gets the BIR type list.
	 * 
	 * @param xmlBytes byte array of XML data
	 * @return the BIR type list
	 * @throws Exception the exception
	 */
	public List<BIR> getBIRDataFromXML(byte[] xmlBytes) throws Exception {
		return cbeffutil.getBIRDataFromXML(xmlBytes);
	}
}
