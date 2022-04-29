package io.mosip.admin.controller;

import io.mosip.admin.packetstatusupdater.dto.ApplicantVerficationDto;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.AdminService;
import io.mosip.kernel.core.http.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Access;

@RestController
public class AdminController {

    @Autowired
    AuditUtil auditUtil;

    @Autowired
    AdminService adminService;

    @GetMapping("/applicantVerficationDetails/{rid}")
    private ResponseWrapper<ApplicantVerficationDto> getApplicantVerficationDetails(@PathVariable("rid") String rid) throws Exception {
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_API_CALLED);
        ResponseWrapper<ApplicantVerficationDto> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(adminService.getApplicantVerficationDetails(rid));
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_SUCCESS);
        return responseWrapper;
    }

}
