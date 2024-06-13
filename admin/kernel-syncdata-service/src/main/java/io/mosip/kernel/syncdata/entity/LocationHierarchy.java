package io.mosip.kernel.syncdata.entity;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import io.mosip.kernel.syncdata.entity.id.LocationHierarchyId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loc_hierarchy_list", schema = "master")
@IdClass(LocationHierarchyId.class)
public class LocationHierarchy extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -854194758755759037L;

	@Id
	@AttributeOverrides({
			@AttributeOverride(name = "hierarchyLevel", column = @Column(name = "hierarchy_level", nullable = false)),
			@AttributeOverride(name = "hierarchyLevelName", column = @Column(name = "hierarchy_level_name", nullable = false, length = 64)),
			@AttributeOverride(name = "langCode", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	private short hierarchyLevel;
	private String hierarchyLevelName;
	private String langCode;
	
}
	

