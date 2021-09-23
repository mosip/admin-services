package io.mosip.kernel.masterdata.controller;

import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.dto.PossibleValueDto;
import io.mosip.kernel.masterdata.service.PossibleValuesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/possiblevalues")
public class PossibleValuesController {

    @Autowired
    private PossibleValuesService possibleValuesService;

    @ResponseFilter
    @PreAuthorize("hasAnyRole(@authorizedRoles.getGetpossiblevaluesfieldname())")
    @GetMapping("/{fieldName}")
    //@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','REGISTRATION_PROCESSOR')")
    @ApiOperation(value = "Service to fetch all possible values of any default / dynamic field")
    public ResponseWrapper<Map<String, List<PossibleValueDto>>> getAllValuesOfField(
            @PathVariable("fieldName") String fieldName,
            @RequestParam(name = "langCode", required = true) @ApiParam(value = "Lang Code", required = true) String langCode) {
        ResponseWrapper<Map<String, List<PossibleValueDto>>> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(possibleValuesService.getAllValuesOfField(fieldName, langCode));
        return responseWrapper;
    }
}
