package io.mosip.hotlist.helper;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.mosip.hotlist.builder.AuditRequestBuilder;
import io.mosip.hotlist.builder.RestRequestBuilder;
import io.mosip.hotlist.constant.AuditEvents;
import io.mosip.hotlist.constant.AuditModules;
import io.mosip.hotlist.constant.RestServicesConstants;
import io.mosip.hotlist.dto.AuditRequestDTO;
import io.mosip.hotlist.dto.AuditResponseDTO;
import io.mosip.hotlist.dto.RestRequestDTO;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.logger.spi.Logger;

/**
 * The Class AuditHelper - helper class that makes async rest call to audit
 * service with provided audit details .
 *
 * @author Manoj SP
 */
@Component
public class AuditHelper {

	/** The mosipLogger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(AuditHelper.class);

	/** The rest helper. */
	@Autowired
	private RestHelper restHelper;

	/** The audit factory. */
	@Autowired
	private AuditRequestBuilder auditBuilder;

	/** The rest factory. */
	@Autowired
	private RestRequestBuilder restBuilder;

	/**
	 * Audit - method to call audit service and store audit details.
	 *
	 * @param module the module
	 * @param event  the event
	 * @param id     the id
	 * @param idType the id type
	 * @param desc   the desc
	 */
	public void audit(AuditModules module, AuditEvents event, String id, String idType, String desc) {
		RequestWrapper<AuditRequestDTO> auditRequest = auditBuilder.buildRequest(module, event,
				HotlistSecurityManager.hash(id.getBytes()), idType, desc);
		RestRequestDTO restRequest;
		try {
			restRequest = restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditRequest,
					AuditResponseDTO.class);
			restHelper.requestAsync(restRequest);
		} catch (HotlistAppException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), "AuditRequestBuilder", "audit",
					"Exception : " + ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), "AuditRequestBuilder", "audit",
					"Exception : " + ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Audit error.
	 *
	 * @param module the module
	 * @param event  the event
	 * @param id     the id
	 * @param idType the id type
	 * @param e      the e
	 */
	public void auditError(AuditModules module, AuditEvents event, String id, String idType, Throwable e) {
		this.audit(module, event, id, idType, ExceptionUtils.getStackTrace(e));
	}

}