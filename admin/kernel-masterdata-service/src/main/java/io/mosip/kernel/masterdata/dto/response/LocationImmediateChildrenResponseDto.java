package io.mosip.kernel.masterdata.dto.response;

import io.mosip.kernel.masterdata.entity.Location;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LocationImmediateChildrenResponseDto {
    Map<String, List<Location>> locations;
}
