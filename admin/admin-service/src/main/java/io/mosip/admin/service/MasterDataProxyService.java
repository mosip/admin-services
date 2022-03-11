package io.mosip.admin.service;

import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.util.MasterdataProxyServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MasterDataProxyService {

	@Autowired
	private MasterdataProxyServiceUtil util;

	@Autowired
	private AuditUtil auditUtil;

	private static final Logger logger = LoggerFactory.getLogger(MasterDataProxyService.class);


	public Object getMasterDataResponse(String body, HttpServletRequest request) {
		logger.info("In getMasterDataResponse of proxymasterdataservice");

		return util.masterDataRestCall(util.getUrl(request), body, util.getHttpMethodType(request));
	}

}
