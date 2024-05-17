package io.mosip.kernel.syncdata.entity;

import io.mosip.kernel.syncdata.entity.id.CodeAndLanguageCodeID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entity class mapping title to master data
 * 
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@NamedNativeQueries({
		@NamedNativeQuery(name = "Title.getThroughLanguageCode", query = "select code, name, descr , lang_code , is_active , cr_by , cr_dtimes , upd_by , upd_dtimes ,is_deleted , del_dtimes from master.title where lang_code = ?1 and (is_deleted is null or is_deleted = false) ", resultClass = Title.class) })
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "title", schema = "master")
public class Title extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1323331283383315822L;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "code", column = @Column(name = "code", nullable = false, length = 16)),
			@AttributeOverride(name = "lang_code", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	@Column(name = "code", unique = true, nullable = false, length = 16)
	private CodeAndLanguageCodeID id;

	@Column(name = "name", unique = true, nullable = false, length = 64)
	private String titleName;

	@Column(name = "descr", unique = true, nullable = false, length = 128)
	private String titleDescription;

}
