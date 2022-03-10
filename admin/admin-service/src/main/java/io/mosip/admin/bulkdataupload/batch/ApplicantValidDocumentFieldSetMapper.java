package io.mosip.admin.bulkdataupload.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import io.mosip.admin.bulkdataupload.entity.ApplicantValidDocument;
import io.mosip.admin.bulkdataupload.entity.id.ApplicantValidDocumentId;

public class ApplicantValidDocumentFieldSetMapper implements FieldSetMapper<ApplicantValidDocument> {

	@Override
	public ApplicantValidDocument mapFieldSet(FieldSet fieldSet) throws BindException {
		ApplicantValidDocument applicantValidDocument = new ApplicantValidDocument();
		ApplicantValidDocumentId applicantValidDocumentId = new ApplicantValidDocumentId();
		applicantValidDocumentId.setAppTypeCode(fieldSet.readString(0));
		applicantValidDocumentId.setDocCategoryCode(fieldSet.readString(1));
		applicantValidDocumentId.setDocTypeCode(fieldSet.readString(2));
		applicantValidDocument.setApplicantValidDocumentId(applicantValidDocumentId);
		applicantValidDocument.setLangCode(fieldSet.readString(3));
		applicantValidDocument.setIsActive(Boolean.parseBoolean(fieldSet.readString(4)));
		applicantValidDocument.setIsDeleted(Boolean.parseBoolean(fieldSet.readString(5)));
		return applicantValidDocument;
	}

}
