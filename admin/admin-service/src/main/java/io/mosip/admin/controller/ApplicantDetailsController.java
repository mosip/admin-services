package io.mosip.admin.controller;

import io.mosip.admin.packetstatusupdater.dto.ApplicantDetailsDto;
import io.mosip.admin.packetstatusupdater.dto.ApplicantUserDetailsDto;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.ApplicantDetailService;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

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

    @GetMapping("/applicantDetails/getLoginDetails")
    private ResponseWrapper<ApplicantUserDetailsDto> getApplicantUserDetails() throws Exception {
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_LOGIN_DETAILS_API_CALLED);
        ResponseWrapper<ApplicantUserDetailsDto> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(applicantDetailService.getApplicantUserDetails());
        auditUtil.setAuditRequestDto(EventEnum.APPLICANT_LOGIN_DETAILS_SUCCESS);
        return responseWrapper;
    }

    @GetMapping("/rid-digital-card/{rid}")
    public ResponseEntity<Object> getRIDDigitalCard(
           @PathVariable("rid") String rid,@RequestParam("isAcknowledged") boolean isAcknowledged) throws Exception {
        auditUtil.setAuditRequestDto(EventEnum.RID_DIGITAL_CARD_REQ);
        byte[] pdfBytes = applicantDetailService.getRIDDigitalCard(rid,isAcknowledged);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));
        auditUtil.setAuditRequestDto(EventEnum.RID_DIGITAL_CARD_REQ_SUCCESS);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
                .header("Content-Disposition", "attachment; filename=\"" +
                        rid + ".pdf\"")
                .body((Object) resource);
    }
}
