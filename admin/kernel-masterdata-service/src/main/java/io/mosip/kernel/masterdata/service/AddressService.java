package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.AddressDto;

public interface AddressService {

    public AddressDto getLatestAddress(AddressDto dto);

    public AddressDto getLatestFHIRAddress(AddressDto dto);
}
