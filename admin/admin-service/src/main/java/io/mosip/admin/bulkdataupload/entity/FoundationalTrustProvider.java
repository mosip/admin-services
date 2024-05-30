/**
 * 
 */
package io.mosip.admin.bulkdataupload.entity;

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
 * @author Ramadurai Pandian
 *
 */
@Data
@Entity
@Table(name = "foundational_trust_provider", schema = "master")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FoundationalTrustProvider extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7547973611404461248L;

	@Id
	private String id;

	/**
	 * The FTP name.
	 */
	@Column(name = "name", nullable = false, length = 36)
	private String name;

	/**
	 * The FTP address.
	 */
	@Column(name = "address", nullable = false, length = 512)
	private String address;

	/**
	 * The FTP email.
	 */
	@Column(name = "email", nullable = false, length = 256)
	private String email;

	/**
	 * The FTP contactNo.
	 */
	@Column(name = "contact_number", nullable = false, length = 256)
	private String contactNo;

	/**
	 * The FTP certAlias.
	 */
	@Column(name = "certificate_alias", nullable = false, length = 256)
	private String certAlias;

}
