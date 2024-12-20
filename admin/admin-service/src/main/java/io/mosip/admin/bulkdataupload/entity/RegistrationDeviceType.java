package io.mosip.admin.bulkdataupload.entity;

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
 * @author Megha Tanga
 *
 */

@Entity
@Table(name = "reg_device_type", schema = "master")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDeviceType extends BaseEntity {

	@Id
	@Column(name = "code", nullable = false, length = 36)
	String code;

	@Column(name = "name", nullable = false, length = 64)
	String name;

	@Column(name = "descr", length = 512)
	String description;

}
