package com.namyesol.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ValidatorTest {

	private Validator validator;
	
	@Before
	public void setUp() {
		try (LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean()) {
			localValidatorFactoryBean.afterPropertiesSet();
			this.validator = localValidatorFactoryBean.getValidator();
		}
	}
	
	@Test
	public void test() {
		
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Owner owner = new Owner();
		owner.setFirstName("");
		owner.setLastName("smith");
		owner.setAddress("address");
		owner.setCity("city");
		owner.setTelephone("telephone");
		
		Set<ConstraintViolation<Owner>> constraintViolations = validator.validate(owner);
		
		assertThat(constraintViolations).hasSize(1);
		ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}

}
