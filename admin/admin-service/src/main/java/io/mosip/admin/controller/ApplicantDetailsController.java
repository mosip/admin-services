package io.mosip.admin.controller;

import io.mosip.admin.packetstatusupdater.dto.ApplicantDetailsDto;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.ApplicantDetailService;
import io.mosip.kernel.core.http.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicantDetailsController {

    @Autowired
    AuditUtil auditUtil;

    @Autowired
    ApplicantDetailService applicantDetailService;

    @GetMapping("/applicantDetails/{rid}")
    private ResponseWrapper<ApplicantDetailsDto> getApplicantDetails(@PathVariable("rid") String rid) throws Exception {
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_API_CALLED);
        ResponseWrapper<ApplicantDetailsDto> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(applicantDetailService.getApplicantDetails(rid));
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_SUCCESS);
        return responseWrapper;
    }

}
