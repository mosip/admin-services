package io.mosip.admin.bulkdataupload.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * 
 * @author dhanendra
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "bulkupload_transaction", schema = "master")
public class BulkUploadTranscation extends BaseEntity{

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id")
	private String id;
	
	@Column(name="entity_name")
	private String entityName;
	
	@Column(name="upload_operation")
	private String uploadOperation;
	
	@Column(name="status_code")
	private String statusCode;
	
	@Column(name="upload_category")
	private String category;
	
	@Column(name="record_count")
	private int recordCount;
	
	@Column(name="uploaded_by")
	private String uploadedBy;
	
	@Column(name="uploaded_dtimes") 
	private Timestamp uploadedDateTime;
	
	@Column(name="upload_description")
	private String uploadDescription;
	
	@Column(name="lang_code")
	private String langCode;

}
