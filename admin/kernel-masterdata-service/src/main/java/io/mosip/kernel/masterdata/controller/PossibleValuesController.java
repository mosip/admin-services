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
    @GetMapping("/dynamic/{fieldName}")
    @PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
    @ApiOperation(value = "Service to fetch all values of any dynamic field")
    public ResponseWrapper<Map<String, List<PossibleValueDto>>> getAllValuesOfDynamicField(
            @PathVariable("fieldName") String fieldName,
            @RequestParam(name = "langCode", required = true) @ApiParam(value = "Lang Code", required = true) String langCode) {
        ResponseWrapper<Map<String, List<PossibleValueDto>>> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(possibleValuesService.getAllValuesOfDynamicField(fieldName, langCode));
        return responseWrapper;
    }

    @ResponseFilter
    @GetMapping("/default/{fieldName}")
    @PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
    @ApiOperation(value = "Service to fetch all values of any dynamic field")
    public ResponseWrapper<Map<String, List<PossibleValueDto>>> getAllValuesOfDefaultField(
            @PathVariable("fieldName") String fieldName,
            @RequestParam(name = "langCode", required = true) @ApiParam(value = "Lang Code", required = true) String langCode) {
        ResponseWrapper<Map<String, List<PossibleValueDto>>> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(possibleValuesService.getAllValuesOfDefaultField(fieldName, langCode));
        return responseWrapper;
    }
}
