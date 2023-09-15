package com.namyesol.petclinic.controller;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.service.ClinicService;

/**
 * Instructs Spring MVC on how to parse and print elements of type 'PetType'. Starting from Spring 3.0, Formatters have
 * come as an improvement in comparison to legacy PropertyEditors. See the following links for more details: - The
 * Spring ref doc: http://static.springsource.org/spring/docs/current/spring-framework-reference/html/validation.html#format-Formatter-SPI
 * - A nice blog entry from Gordon Dickens: http://gordondickens.com/wordpress/2010/09/30/using-spring-3-0-custom-type-converter/
 * <p/>
 * Also see how the bean 'conversionService' has been declared inside /WEB-INF/dispatcher-servlet.xml
 */
public class PetTypeFormatter implements Formatter<PetType>{

	private final ClinicService clinicService;
	
	@Autowired
	public PetTypeFormatter(ClinicService clinicService) {
		this.clinicService = clinicService;
	}

	@Override
	public PetType parse(String text, Locale locale) throws ParseException {
		Collection<PetType> petTypes = this.clinicService.findPetTypes();
		for (PetType petType : petTypes) {
			if (petType.getName().equals(text)) {
				return petType;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}

	@Override
	public String print(PetType petType, Locale locale) {
		return petType.getName();
	}
	
	
	
}
