package io.mosip.admin.service;

import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.util.AdminProxyServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AdminProxyService {

	@Autowired
	private AdminProxyServiceUtil util;

	@Autowired
	private AuditUtil auditUtil;

	private static final Logger logger = LoggerFactory.getLogger(AdminProxyService.class);


	public Object getResponse(String body, HttpServletRequest request,String url) {
		logger.info("In getResponse of AdminProxyService");

		return util.adminRestCall(util.getUrl(request,url), body, util.getHttpMethodType(request));
	}

}
