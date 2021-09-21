package io.mosip.kernel.masterdata.dto;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoAutoStart
public class DocMissingData {

	private String id;
	private String fieldValue;
	private String langCode;

}
