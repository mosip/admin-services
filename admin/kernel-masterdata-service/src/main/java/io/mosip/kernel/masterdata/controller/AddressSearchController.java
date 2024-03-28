package io.mosip.kernel.masterdata.controller;

import io.mosip.kernel.core.exception.BaseUncheckedException;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.dto.AddressDto;
import io.mosip.kernel.masterdata.service.AddressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RefreshScope
@RestController
@Tag(name = "Latest Address Search", description = "Latest Address Search")
public class AddressSearchController {

    @Autowired
    AddressService addressService;

    @ResponseFilter
    @PostMapping(value = "/latestAddress")
    public ResponseWrapper<AddressDto> latestAddress(@Valid @RequestBody RequestWrapper<AddressDto> dto) {
        ResponseWrapper<AddressDto> response = new ResponseWrapper<>();
        try {
            AddressDto addressDto = addressService.getLatestAddress(dto.getRequest());
            response.setResponse(addressDto);
        } catch (BaseUncheckedException e) {
            ServiceError err =  new ServiceError(e.getErrorCode(), e.getMessage());
            List<ServiceError> li = new ArrayList<>();
            li.add(err);
            response.setErrors(li);
        }
        return response;
    }

    @ResponseFilter
    @PostMapping(value = "/latestFhirAddress")
    public ResponseWrapper<AddressDto> latestFhirAddress(@Valid @RequestBody RequestWrapper<AddressDto> dto) {
        ResponseWrapper<AddressDto> response = new ResponseWrapper<>();
        try {
            AddressDto addressDto = addressService.getLatestFHIRAddress(dto.getRequest());
            response.setResponse(addressDto);
        } catch (BaseUncheckedException e) {
            ServiceError err = new ServiceError(e.getErrorCode(), e.getMessage());
            List<ServiceError> li = new ArrayList<>();
            li.add(err);
            response.setErrors(li);
        }
        return response;
    }
}
