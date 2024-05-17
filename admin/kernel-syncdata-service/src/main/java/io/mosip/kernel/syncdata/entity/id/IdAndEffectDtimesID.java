package io.mosip.kernel.syncdata.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdAndEffectDtimesID implements Serializable {

	private static final long serialVersionUID = 7001663925687776491L;

	@Column(name = "id", nullable = false)
	private String id;

	@Column(name = "eff_dtimes", nullable = false)
	private LocalDateTime effectDtimes;

}
