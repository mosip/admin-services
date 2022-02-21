package io.mosip.kernel.masterdata.test.validator;

import static org.junit.Assert.assertEquals;

import javax.validation.ConstraintValidatorContext;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.validator.SpecialCharacterValidator;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpecialCharacterValidatorTest {
	
	private SpecialCharacterValidator validator;

	@Mock
	ConstraintValidatorContext context;
	@Value("${mosip.kernel.masterdata.code.validate.regex:[^a-z0-9]}")
	private String allowedCharactersRegex;

	
	@Before
	public void setup() {
		validator = new SpecialCharacterValidator();
		validator.setAllowedCharactersRegex(allowedCharactersRegex);
	}
	
	
	@Test
	public void testValidate1() {
		assertEquals(validator.isValid("#$@#$",context),false);
	}
	

	@Test
	public void testValidate2() {
		
		assertEquals(validator.isValid("1234",context),true);
	}
	
	
	@Test
	public void testValidate3() {
		assertEquals(validator.isValid("abcd",context),true);
	}
	
	@Test
	public void testValidate4() {
		assertEquals(validator.isValid("@#abcd",context),false);
	}
	
	@Test
	public void testValidate5() {
		assertEquals(validator.isValid("ab@#cd",context),false);
	}
	
	@Test
	public void testValidate6() {
		assertEquals(validator.isValid("abcd#$@#",context),false);
	}
	
	@Test
	public void testValidate7() {
		assertEquals(validator.isValid("abcd123123",context),true);
	}
}
