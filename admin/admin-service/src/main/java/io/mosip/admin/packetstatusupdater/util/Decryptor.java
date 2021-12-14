package io.mosip.admin.packetstatusupdater.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.admin.bulkdataupload.constant.CryptomanagerConstant;
import io.mosip.admin.bulkdataupload.service.impl.BulkDataUploadServiceImpl;
import io.mosip.admin.dto.CryptomanagerRequestDto;
import io.mosip.admin.dto.CryptomanagerResponseDto;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.registration.processor.core.code.*;
import io.mosip.registration.processor.core.constant.LoggerFileConstant;
import io.mosip.registration.processor.core.exception.ApisResourceAccessException;
import io.mosip.registration.processor.core.exception.util.PlatformErrorMessages;
import io.mosip.registration.processor.core.exception.util.PlatformSuccessMessages;
import io.mosip.registration.processor.core.http.ResponseWrapper;
import io.mosip.registration.processor.core.logger.LogDescription;
import io.mosip.registration.processor.core.logger.RegProcessorLogger;
import io.mosip.registration.processor.core.spi.restclient.RegistrationProcessorRestClientService;
import io.mosip.registration.processor.packet.manager.constant.CryptomanagerConstant;
import io.mosip.registration.processor.rest.client.audit.builder.AuditLogRequestBuilder;
import io.mosip.registration.processor.status.constants.PacketDecryptionFailureExceptionConstant;
import io.mosip.registration.processor.status.dto.CryptomanagerRequestDto;
import io.mosip.registration.processor.status.dto.CryptomanagerResponseDto;
import io.mosip.registration.processor.status.exception.PacketDecryptionFailureException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Decryptor class for packet decryption.
 *
 * @author Girish Yarru
 */

public class Decryptor {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Decryptor.class);

	private static final String KEY = "data";

	@Value("${registration.processor.application.id}")
	private String applicationId;

	@Value("${crypto.PrependThumbprint.enable:true}")
	private boolean isPrependThumbprintEnabled;

	@Autowired
	private RestClient restClientService;

	@Autowired
	private AuditLogRequestBuilder auditLogRequestBuilder;

	@Autowired
	private Environment env;

	@Autowired
	private ObjectMapper mapper;

	private static final String DECRYPT_SERVICE_ID = "mosip.registration.processor.crypto.decrypt.id";
	private static final String REG_PROC_APPLICATION_VERSION = "mosip.registration.processor.application.version";
	private static final String DATETIME_PATTERN = "mosip.registration.processor.datetime.pattern";

	private static final String DECRYPTION_SUCCESS = "Decryption success";
	private static final String DECRYPTION_FAILURE = "Virus scan decryption failed for  registrationId ";
	private static final String IO_EXCEPTION = "Exception Converting encrypted packet inputStream to string";

	/**
	 * This method consumes inputStream of encrypted packet and registrationId as
	 * arguments. Hits the kernel's crypto-manager api passing 'application
	 * id,center id and encrypted inputStream in form of string. gets the
	 * response(Success or Failure) as string if success convert string to
	 * cryptomanager response dto and then get decrypted data and then return
	 * inputStream of decrypted data. if failure convert string to cryptomanager
	 * response dto and then get error code and error response and throw
	 * PacketDecryptionFailureException.
	 * 
	 * @param encryptedSyncMetaInfo
	 * @return
	 * @throws PacketDecryptionFailureException
	 * @throws ApisResourceAccessException
	 * @throws ParseException
	 */

	@SuppressWarnings("unchecked")
	public String decrypt(Object encryptedSyncMetaInfo, String referenceId, String timeStamp)
			throws PacketDecryptionFailureException, ApisResourceAccessException {
		String decryptedData = null;
		boolean isTransactionSuccessful = false;
		try {
			byte[] packet = CryptoUtil.decodeBase64(encryptedSyncMetaInfo.toString());
			CryptomanagerRequestDto cryptomanagerRequestDto = new CryptomanagerRequestDto();
			cryptomanagerRequestDto.setPrependThumbprint(isPrependThumbprintEnabled);
			RequestWrapper<CryptomanagerRequestDto> request = new RequestWrapper<>();
			cryptomanagerRequestDto.setApplicationId(applicationId);
			cryptomanagerRequestDto.setReferenceId(referenceId);
			byte[] nonce = Arrays.copyOfRange(packet, 0, CryptomanagerConstant.GCM_NONCE_LENGTH);
			byte[] aad = Arrays.copyOfRange(packet, CryptomanagerConstant.GCM_NONCE_LENGTH,
					CryptomanagerConstant.GCM_NONCE_LENGTH + CryptomanagerConstant.GCM_AAD_LENGTH);
			byte[] encryptedData = Arrays.copyOfRange(packet, CryptomanagerConstant.GCM_NONCE_LENGTH + CryptomanagerConstant.GCM_AAD_LENGTH,
					packet.length);
			cryptomanagerRequestDto.setAad(CryptoUtil.encodeBase64String(aad));
			cryptomanagerRequestDto.setSalt(CryptoUtil.encodeBase64String(nonce));
			cryptomanagerRequestDto.setData(CryptoUtil.encodeBase64String(encryptedData));
			DateTimeFormatter format = DateTimeFormatter.ofPattern(env.getProperty(DATETIME_PATTERN));
			LocalDateTime time = LocalDateTime.parse(timeStamp, format);
			cryptomanagerRequestDto.setTimeStamp(time);
			// setLocal Date Time
			request.setId(env.getProperty(DECRYPT_SERVICE_ID));
			request.setMetadata(null);
			request.setRequest(cryptomanagerRequestDto);
			LocalDateTime localdatetime = LocalDateTime
					.parse(DateUtils.getUTCCurrentDateTimeString(env.getProperty(DATETIME_PATTERN)), format);
			request.setRequesttime(localdatetime);
			request.setVersion(env.getProperty(REG_PROC_APPLICATION_VERSION));

			ResponseWrapper<CryptomanagerResponseDto> response = (ResponseWrapper<CryptomanagerResponseDto>) restClientService
					.postApi(ApiName.CRYPTOMANAGERDECRYPT, "", "", request, ResponseWrapper.class);
			if (response.getResponse() != null) {
				LinkedHashMap responseMap = mapper.readValue(mapper.writeValueAsString(response.getResponse()),
						LinkedHashMap.class);
				byte[] decryptedPacket = CryptoUtil.decodeBase64(responseMap.get(KEY).toString());
				decryptedData = new String(decryptedPacket);
			} else {
				description.setMessage(PlatformErrorMessages.RPR_PDS_PACKET_DECRYPTION_FAILURE.getMessage());
				description.setCode(PlatformErrorMessages.RPR_PDS_PACKET_DECRYPTION_FAILURE.getCode());
				logger.error(LoggerFileConstant.SESSIONID.toString(),
						LoggerFileConstant.REGISTRATIONID.toString(), "", IO_EXCEPTION);
				throw new PacketDecryptionFailureException(response.getErrors().get(0).getErrorCode(),
						response.getErrors().get(0).getMessage());
			}

			isTransactionSuccessful = true;
			description.setMessage(PlatformSuccessMessages.RPR_DECRYPTION_SUCCESS.getMessage());
			description.setCode(PlatformSuccessMessages.RPR_DECRYPTION_SUCCESS.getCode());
			logger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
					"", "Decryptor::decrypt()::exit");
			logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
					"", description.getMessage());
		} catch (IOException e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
					"", IO_EXCEPTION);
			description.setMessage(PlatformErrorMessages.RPR_PDS_IO_EXCEPTION.getMessage());
			description.setCode(PlatformErrorMessages.RPR_PDS_IO_EXCEPTION.getCode());

			throw new PacketDecryptionFailureException(
					PacketDecryptionFailureExceptionConstant.MOSIP_PACKET_DECRYPTION_FAILURE_ERROR_CODE.getErrorCode(),
					IO_EXCEPTION);
		} catch (ApisResourceAccessException e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
					"", "Internal Error occurred " + ExceptionUtils.getStackTrace(e));

		}

		logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "",
				DECRYPTION_SUCCESS);
		return decryptedData;
	}

}
