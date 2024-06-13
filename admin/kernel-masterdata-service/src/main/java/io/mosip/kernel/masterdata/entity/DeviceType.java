package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * Entity for Device Type Details
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_type", schema = "master")
public class DeviceType extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8541947587557590379L;

	@Id

	@AttributeOverride(name = "code", column = @Column(name = "code", nullable = false, length = 36))
	private String code;

	@Column(name = "lang_code", length = 3)
	private String langCode;

	@Column(name = "name", nullable = false, length = 64)
	private String name;

	@Column(name = "descr", length = 128)
	private String description;
}
