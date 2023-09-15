package com.namyesol.petclinic.controller;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.namyesol.petclinic.domain.Pet;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such validation rule in Java.
 * </p>
 *
 */
public class PetValidator implements Validator {

	private static final String REQUIRED = "required";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Pet pet = (Pet) target;
		String name = pet.getName();
		if (!StringUtils.hasLength(name)) {
			errors.rejectValue(name, REQUIRED, REQUIRED);
		}
		
		if (pet.isNew() && pet.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}
		
		if (pet.getBirthDate() == null) {
			errors.rejectValue("birthDate", REQUIRED, REQUIRED);
		}
	}

}
