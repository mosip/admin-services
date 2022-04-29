package io.mosip.kernel.masterdata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilterResult<T> {

    List<T> filterData;
    long totalCount;

}
