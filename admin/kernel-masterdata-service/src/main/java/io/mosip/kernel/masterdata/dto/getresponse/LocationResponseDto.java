package io.mosip.kernel.masterdata.dto.getresponse;

import java.util.List;

import io.mosip.kernel.masterdata.dto.LocationDto;
import lombok.Data;

@Data

public class LocationResponseDto {

	private List<LocationDto> locations;
}
