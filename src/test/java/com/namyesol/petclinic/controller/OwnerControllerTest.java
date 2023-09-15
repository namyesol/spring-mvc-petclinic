package com.namyesol.petclinic.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.service.ClinicService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml", "classpath:mvc-test-config.xml"})
@WebAppConfiguration
public class OwnerControllerTest {

	private static final int TEST_OWNER_ID = 1;
	
	@Autowired
	private OwnerController ownerController;
	
	@Autowired
	private ClinicService clinicService;
	
	private MockMvc mockMvc;
	
	private Owner george;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
		
		george = new Owner();
        george.setId(TEST_OWNER_ID);
        george.setFirstName("George");
        george.setLastName("Franklin");
        george.setAddress("110 W. Liberty St.");
        george.setCity("Madison");
        george.setTelephone("6085551023");
        
        given(this.clinicService.findOwnerById(TEST_OWNER_ID)).willReturn(george);
	}
	
	@Test
	public void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("owner"))
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}
	
	@Test
	public void testProcessCreationFormSucess() throws Exception {
		mockMvc.perform(post("/owners/new")
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
	            .param("city", "London")
	            .param("telephone", "01316761638"))
			.andExpect(status().is3xxRedirection());	
	}
	
	@Test
	public void testProcessCreationFormHasError() throws Exception {
		mockMvc.perform(post("/owners/new")
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("city", "London"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("owner"))
			.andExpect(model().attributeHasFieldErrors("owner", "address"))
			.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}
	
	@Test
	public void testInitFindForm() throws Exception {
		mockMvc.perform(get("/owners/find"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("owner"))
			.andExpect(view().name("owners/findOwners"));
	}
	
	@Test
	public void testProcessFindFormSuccess() throws Exception {
		given(clinicService.findOwnerByLastName("")).willReturn(Lists.newArrayList(george, new Owner()));
		
		mockMvc.perform(get("/owners"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/ownersList"));
	}
	
	@Test
	public void testProcessFindFormByLastName() throws Exception {
		given(clinicService.findOwnerByLastName(george.getLastName())).willReturn(Lists.newArrayList(george));
		
		mockMvc.perform(get("/owners")
			.param("lastName", "Franklin")
		)
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID));
	}
	
	@Test
	public void testProcessFindFormNoOwnersFound() throws Exception {
		mockMvc.perform(get("/owners")
			.param("lastName", "Unkown Surname")
		)
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("owner", "lastName"))
			.andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
			.andExpect(view().name("owners/findOwners"));
	}
	
	@Test
	public void testInitUpdateOwnerForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("owner"))
            .andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
            .andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
            .andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
            .andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
            .andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}
	
	@Test
    public void testProcessUpdateOwnerFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)
            .param("firstName", "Joe")
            .param("lastName", "Bloggs")
            .param("address", "123 Caramel Street")
            .param("city", "London")
            .param("telephone", "01616291589")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/{ownerId}"));
    }
	
	@Test
	public void testProcessUpdateOwnerFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)
	            .param("firstName", "Joe")
	            .param("lastName", "Bloggs")
	            .param("city", "London")
	        )
	            .andExpect(status().isOk())
	            .andExpect(model().attributeHasErrors("owner"))
	            .andExpect(model().attributeHasFieldErrors("owner", "address", "telephone"))
	            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}
	
	@Test
	public void testShowOwner() throws Exception {
		 mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID))
	         .andExpect(status().isOk())
	         .andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
	         .andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
	         .andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
	         .andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
	         .andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
	         .andExpect(view().name("owners/ownerDetails"));
	}
}
