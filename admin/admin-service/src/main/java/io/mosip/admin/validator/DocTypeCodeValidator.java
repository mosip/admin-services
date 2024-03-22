package io.mosip.admin.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import io.mosip.admin.bulkdataupload.repositories.DocumentTypeRepository;

public class DocTypeCodeValidator implements ConstraintValidator<DocTypeCode, String>  {

	private List<String> docTypeCode;

	@Autowired
	DocumentTypeRepository documentTypeRepository;
	

	@Override
	public void initialize(DocTypeCode constraintAnnotation) {
		if(documentTypeRepository == null){
			/*         Note: An additional validation was getting triggered by doInvoke() method of 
			 * 		 RepositoryListItemWriter class with documentTypeRepository equal to null
			 *               which is not desired. This if clause is being used to escape that additional 
			 *               validation step.
			 */
			return;
		}
		docTypeCode = documentTypeRepository.findAllByIsDeletedFalseOrIsDeletedIsNull();
	}



	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(documentTypeRepository == null){
			/*         Note: An additional validation was getting triggered by doInvoke() method of 
			 * 		 RepositoryListItemWriter class with documentTypeRepository equal to null
			 *               which is not desired. This if clause is being used to escape that additional 
			 *               validation step.
			 */
			return true;
		}
		
		if(null != value && !value.isEmpty()) {
			return docTypeCode.contains(value);
		}
		return false;
	}
	

}
