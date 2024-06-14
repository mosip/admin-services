package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.CodeAndLanguageCodeID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * Entity for Individual type
 * 
 * @author Bal Vikash Sharma
 * @author Sidhant Agarwal
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "individual_type", schema = "master")
@IdClass(CodeAndLanguageCodeID.class)
public class IndividualType extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5585825705521742941L;

	/**
	 * Field for individual type code
	 */
	@Id
	@Column(name = "code")
	private String code;

	@Id
	@Column(name = "lang_code", nullable = false)
	private String langCode;

	/**
	 * Field for individual type name
	 */
	@Column(name = "name", nullable = false, length = 64)
	private String name;

}