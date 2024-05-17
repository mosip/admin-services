package io.mosip.kernel.syncdata.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity class for blacklisted words.
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
	 * The blacklisted word.
	 */
	@Id
	@Column(name = "word", length = 128)
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
