package io.mosip.kernel.syncdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response class for Biometric attribute save
 * 
 * @author Uday Kumar
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BioTypeCodeAndLangCodeAndAttributeCode extends BaseDto {
	private String code;
	private String biometricTypeCode;

}
