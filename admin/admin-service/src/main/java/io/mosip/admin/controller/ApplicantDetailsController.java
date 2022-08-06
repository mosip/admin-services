package io.mosip.admin.controller;

import io.mosip.admin.dto.ApplicantDetailsDto;
import io.mosip.admin.dto.ApplicantUserDetailsDto;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.ApplicantDetailService;
import io.mosip.kernel.core.http.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
public class ApplicantDetailsController {

    @Autowired
    AuditUtil auditUtil;

    @Autowired
    ApplicantDetailService applicantDetailService;

    @PreAuthorize("hasRole('DIGITALCARD_ADMIN')")
    @GetMapping("/applicantDetails/{rid}")
    public ResponseWrapper<ApplicantDetailsDto> getApplicantDetails(@PathVariable("rid") String rid) throws Exception {
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_API_CALLED,null);
        ResponseWrapper<ApplicantDetailsDto> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(applicantDetailService.getApplicantDetails(rid));
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_SUCCESS,null);
        return responseWrapper;
    }

    @PreAuthorize("hasRole('DIGITALCARD_ADMIN')")
    @GetMapping("/applicantDetails/getLoginDetails")
    public ResponseWrapper<ApplicantUserDetailsDto> getApplicantUserDetails() throws Exception {
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_LOGIN_DETAILS_API_CALLED,null);
        ResponseWrapper<ApplicantUserDetailsDto> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(applicantDetailService.getApplicantUserDetails());
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_LOGIN_DETAILS_SUCCESS,null);
        return responseWrapper;
    }

    @PreAuthorize("hasRole('DIGITALCARD_ADMIN')")
    @GetMapping("/rid-digital-card/{rid}")
    public ResponseEntity<Object> getRIDDigitalCard(
           @PathVariable("rid") String rid,@RequestParam("isAcknowledged") boolean isAcknowledged) throws Exception {
        auditUtil.setAuditRequestDto(EventEnum.RID_DIGITAL_CARD_REQ,null);
        byte[] pdfBytes = applicantDetailService.getRIDDigitalCard(rid,isAcknowledged);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));
        auditUtil.setAuditRequestDto(EventEnum.RID_DIGITAL_CARD_REQ_SUCCESS,null);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
                .header("Content-Disposition", "attachment; filename=\"" +
                        rid + ".pdf\"")
                .body((Object) resource);
    }
}
