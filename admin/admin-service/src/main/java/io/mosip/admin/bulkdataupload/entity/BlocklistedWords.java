package io.mosip.admin.bulkdataupload.entity;

import io.mosip.admin.validator.AlphabeticValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Entity class for blocklisted words.
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
	@AlphabeticValidator(message = "Blocklisted word can only contain Alphabets")
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
