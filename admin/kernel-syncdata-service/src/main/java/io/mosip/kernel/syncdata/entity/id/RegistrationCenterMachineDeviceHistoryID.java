package io.mosip.kernel.syncdata.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RegistrationCenterMachineDeviceHistoryID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8092065929310769990L;

	@Column(name = "regcntr_id", unique = true, nullable = false, length = 10)
	private String regCenterId;

	@Column(name = "device_id", unique = true, nullable = false, length = 36)
	private String deviceId;

	@Column(name = "machine_id", unique = true, nullable = false, length = 10)
	private String machineId;

	@Column(name = "eff_dtimes", nullable = false)
	private LocalDateTime effectivetimes;
}
