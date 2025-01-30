package io.mosip.admin.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import io.mosip.admin.bulkdataupload.repositories.DocumentTypeRepository;

public class DocTypeCodeValidator implements ConstraintValidator<DocTypeCode, String>  {

	@Autowired
	private List<String> docTypeCodes;


	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(docTypeCodes == null){
			/*         Note: An additional validation was getting triggered by doInvoke() method of 
			 * 		 RepositoryListItemWriter class with documentTypeRepository equal to null
			 *               which is not desired. This if clause is being used to escape that additional 
			 *               validation step.
			 */
			return true;
		}
		
		if(null != value && !value.isEmpty()) {
			return docTypeCodes.contains(value);
		}
		return false;
	}
	

}
