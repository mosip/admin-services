package io.mosip.kernel.syncdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.mosip.kernel.clientcrypto.constant.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCenterMachineDto extends BaseDto {

	@NotNull
	@Size(min = 1, max = 36)
	private String regCenterId;

	@NotNull
	@Size(min = 1, max = 36)
	private String machineId;

	@NotNull
	private String publicKey;

	@NotNull
	private String machineSpecId;

	@NotNull
	private String machineTypeId;

	@NotNull
	private ClientType clientType;

}
