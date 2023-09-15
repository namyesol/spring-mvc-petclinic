package com.namyesol.petclinic.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.service.ClinicService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml", "classpath:mvc-test-config.xml"})
@WebAppConfiguration
public class PetControllerTest {

    private static final int TEST_OWNER_ID = 1;
    private static final int TEST_PET_ID = 1;
    
    @Autowired
    private PetController petController;
    
    @Autowired
    private FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean;
    
    @Autowired
    private ClinicService clinicService;
    
    private MockMvc mockMvc;
    
    @Before
    public void setup() {
    	this.mockMvc = MockMvcBuilders
    			.standaloneSetup(petController)
    			.setConversionService(formattingConversionServiceFactoryBean.getObject())
    			.build();
    	
    	PetType cat = new PetType();
    	cat.setId(3);
    	cat.setName("hamster");
    	given(clinicService.findPetTypes()).willReturn(Lists.newArrayList(cat));
    	given(clinicService.findOwnerById(TEST_OWNER_ID)).willReturn(new Owner());
    	given(clinicService.findPetById(TEST_PET_ID)).willReturn(new Pet());
    }
    
    @Test
    public void testInitCreationForm() throws Exception {
    	mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"))
    		.andExpect(model().attributeExists("pet"));
    }
    
    @Test
    public void testProcessCreationFormSuccess() throws Exception {
    	mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
		    .param("name", "Betty")
			.param("type", "hamster")
			.param("birthDate", "2015/02/12")
		)
    		.andExpect(status().is3xxRedirection())
    		.andExpect(view().name("redirect:/owners/{ownerId}"));
    }
    
    @Test
    public void testProcessCreationFormHasErrors() throws Exception {
    	mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
    		    .param("name", "Betty")
    			.param("birthDate", "2015/02/12")
    		)
        		.andExpect(status().isOk())
        		.andExpect(model().attributeHasNoErrors("owner"))
        		.andExpect(model().attributeHasErrors("pet"))
        		.andExpect(model().attributeHasFieldErrors("pet", "type"))
        		.andExpect(view().name("pets/createOrUpdatePetForm"));
    }
    
    @Test
    public void testInitUpdateForm() throws Exception {
    	mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
    }
    
    @Test
    public void testProcessUpdateFormSuccess() throws Exception {
    	mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
			.param("name", "Betty")
			.param("type", "hamster")
			.param("birthDate", "2015/02/12")
		)
    		.andExpect(status().is3xxRedirection())
    		.andExpect(view().name("redirect:/owners/{ownerId}"));
    }
    
    @Test
    public void testProcessUpdateFormHasErrors() throws Exception {
    	mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
    			.param("name", "Betty")
    			.param("birthDate", "2015/02/12")
    		)
    			.andExpect(model().attributeHasNoErrors("owner"))
    			.andExpect(model().attributeHasErrors("pet"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("pets/createOrUpdatePetForm"));
    }
}
