package com.namyesol.petclinic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.service.ClinicService;

@RunWith(MockitoJUnitRunner.class)
public class PetTypeFormatterTest {

	@Mock
	private ClinicService clinicService;
	
	private PetTypeFormatter petTypeFormatter;
	
	@Before
	public void setup() {
		petTypeFormatter = new PetTypeFormatter(clinicService);
	}
	
	@Test
	public void testPrint() {
		PetType petType = new PetType();
		petType.setName("Hamster");
		String petTypeName = petTypeFormatter.print(petType, Locale.ENGLISH);
		assertThat(petTypeName).isEqualTo("Hamster");
	}
	
	@Test
	public void shouldParse() throws ParseException {
		when(clinicService.findPetTypes()).thenReturn(makePetTypes());
		
		PetType petType = petTypeFormatter.parse("Bird", Locale.ENGLISH);
		assertThat(petType.getName()).isEqualTo("Bird");
	}	
	@Test
	public void shouldThrowParseException() {
		when(clinicService.findPetTypes()).thenReturn(makePetTypes());
		
		assertThatThrownBy(() -> {
			petTypeFormatter.parse("Fish", Locale.ENGLISH);		
		}).isInstanceOf(ParseException.class);
	}

	private Collection<PetType> makePetTypes() {
		Collection<PetType> petTypes = new ArrayList<>();
		
		petTypes.add(new PetType(){
			{
				setName("Dog");
			}
		});
		
        petTypes.add(new PetType(){
            {
                setName("Bird");
            }
        });
		return petTypes;
	}
}
