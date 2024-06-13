package io.mosip.kernel.syncdata.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entity for composite primary key in gender table in DB
 * 
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderID implements Serializable {

	private static final long serialVersionUID = -1169678225048676557L;

	@Column(name = "code", unique = true, nullable = false, length = 16)
	private String genderCode;

	@Column(name = "name", unique = true, nullable = false, length = 64)
	private String genderName;

}
