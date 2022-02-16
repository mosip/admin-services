package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.mosip.kernel.masterdata.validator.CharacterValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity class for blaocklisted words.
 * 
 * @author Abhishek Kumar
 * @author Sagar Mahapatra
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blocklisted_words", schema = "master")
@EqualsAndHashCode(callSuper = true)
public class BlocklistedWords extends BaseEntity implements Serializable {

	/**
	 * Serialized version ID.
	 */
	private static final long serialVersionUID = -402658536057675404L;

	/**
	 * The blocklisted word.
	 */
	@Id
	@Column(name = "word", length = 128)
	@CharacterValidator(message = "Blocklisted word cannot contain special characters")
	private String word;

	/**
	 * The language code of the word.
	 */
	@Column(name = "lang_code", length = 3)
	private String langCode;

	/**
	 * The description of the word.
	 */
	@Column(name = "descr", length = 256)
	private String description;
}
