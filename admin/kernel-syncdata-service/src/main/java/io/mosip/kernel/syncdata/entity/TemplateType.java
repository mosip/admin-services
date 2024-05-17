package io.mosip.kernel.syncdata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Uday Kumar
 * @since 1.0.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "template_type", schema = "master")
public class TemplateType extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -854194758755759037L;

	@Id
	@AttributeOverride(name = "code", column = @Column(name = "code", nullable = false, length = 36))
	private String code;

	@Column(name = "lang_code", length = 3)
	private String langCode;

	@Column(name = "descr", length = 256)
	private String description;
}
