package io.mosip.admin.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.mosip.admin.bulkdataupload.repositories.DocumentCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DocCatCodeValidator implements ConstraintValidator<DocCatCode, String> {
	
	@Autowired
	private List<String> docCatCodes;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if(docCatCodes == null){
			/*         Note: An additional validation was getting triggered by doInvoke() method of 
			 * 		 RepositoryListItemWriter class with documentCategoryRepository equal to null
			 *               which is not desired. This if clause is being used to escape that additional 
			 *               validation step.
			 */
			return true;
		}

		if(null != value && !value.isEmpty()) {
			return docCatCodes.contains(value);
		}
		return false;
	}

	
}
