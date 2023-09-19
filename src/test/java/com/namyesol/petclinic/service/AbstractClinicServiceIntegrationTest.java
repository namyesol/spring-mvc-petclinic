package com.namyesol.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.domain.Vet;
import com.namyesol.petclinic.domain.Visit;

abstract class AbstractClinicServiceIntegrationTest {

	@Autowired
	protected ClinicService clinicService;
	
	@Test
	public void shouldFindOwnersByLastName() {
		Collection<Owner> owners = clinicService.findOwnerByLastName("Davis");
		assertThat(owners).hasSize(2);
		
		owners = clinicService.findOwnerByLastName("Daviss");
		assertThat(owners).isEmpty();
	}
	
	@Test
	public void shouldFindSingleOwnerWithPet() {
		Owner owner = clinicService.findOwnerById(1);
		assertThat(owner.getLastName()).startsWith("Franklin");
		assertThat(owner.getPets()).hasSize(1);
		assertThat(owner.getPets().get(0).getType()).isNotNull();
		assertThat(owner.getPets().get(0).getType().getName()).isEqualTo("cat");
	}
	
	@Test
	@Transactional
	public void shouldInsertOwner() {
		Collection<Owner> owners = clinicService.findOwnerByLastName("Schultz");
		int found = owners.size();
		
		Owner owner = new Owner();
        owner.setFirstName("Sam");
        owner.setLastName("Schultz");
        owner.setAddress("4, Evans Street");
        owner.setCity("Wollongong");
        owner.setTelephone("4444444444");
        clinicService.saveOwner(owner);
        assertThat(owner.getId().longValue()).isNotEqualTo(0);
        
        owners = clinicService.findOwnerByLastName("Schultz");
        assertThat(owners).hasSize(found + 1);
	}
	
	@Test
	@Transactional
	public void shouldUpdateOwner() {
		Owner owner = clinicService.findOwnerById(1);
		String oldLastName = owner.getLastName();
		String newLastName = oldLastName + "X";
		
		owner.setLastName(newLastName);
		clinicService.saveOwner(owner);
		
		owner = clinicService.findOwnerById(1);
		assertThat(owner.getLastName()).isEqualTo(newLastName);
	}
	
	@Test
	public void shouldFindPetWithCorrectId() {
		Pet pet7 = clinicService.findPetById(7);
		assertThat(pet7.getName()).startsWith("Samantha");
		assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");
	}
	
	@Test
	public void shouldFindAllPetTypes() {
		Collection<PetType> petTypes = clinicService.findPetTypes();
		PetType petType1 = petTypes.stream().filter(petType -> petType.getId().equals(1)).findAny().get();
		assertThat(petType1.getName()).isEqualTo("cat");
		PetType petType4 = petTypes.stream().filter(petType -> petType.getId().equals(4)).findAny().get();
		assertThat(petType4.getName()).isEqualTo("snake");
	}
	
	@Test
	@Transactional
	public void shouldInsertPetIntoDatabaseAndGenerateId() {
		Owner owner6 = clinicService.findOwnerById(6);
		int found = owner6.getPets().size();
		
		Pet pet = new Pet();
        pet.setName("bowser");
        Collection<PetType> petTypes = clinicService.findPetTypes();
        pet.setType(petTypes.stream().filter(petType -> petType.getId().equals(2)).findAny().get());
        pet.setBirthDate(LocalDate.now());
        owner6.addPet(pet);
        assertThat(owner6.getPets()).hasSize(found + 1);
        
        clinicService.savePet(pet);
        clinicService.saveOwner(owner6);
        
        owner6 = clinicService.findOwnerById(6);
        assertThat(owner6.getPets()).hasSize(found + 1);
        assertThat(pet.getId()).isNotNull();
	}
	
	@Test
	@Transactional
	public void shouldUpdatePetName() throws Exception {
		Pet pet7 = clinicService.findPetById(7);
		String oldName = pet7.getName();
		String newName = oldName + "X";
		
		pet7.setName(newName);
		clinicService.savePet(pet7);
		
		pet7= clinicService.findPetById(7);
		assertThat(pet7.getName()).isEqualTo(newName);
	}
	
	@Test
    public void shouldFindVets() {
        Collection<Vet> vets = this.clinicService.findVets();

        Vet vet = vets.stream().filter(v -> v.getId().equals(3)).findAny().get();
        assertThat(vet.getLastName()).isEqualTo("Douglas");
        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
        assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
        assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
    }
	
	@Test
	@Transactional
	public void shouldAddNewVisitForPet() {
		Pet pet7 = clinicService.findPetById(7);
		int found = pet7.getVisits().size();
		
		Visit visit = new Visit();
		pet7.addVisit(visit);
		visit.setDescription("test");
		
		clinicService.saveVisit(visit);
		clinicService.savePet(pet7);
		
		pet7 = clinicService.findPetById(7);
		assertThat(pet7.getVisits()).hasSize(found + 1);
		assertThat(visit.getId()).isNotNull();
	}
	
	@Test
	public void shouldFindVisitsByPetId() throws Exception {
		Collection<Visit> visits = clinicService.findVisitsByPetId(7);
		assertThat(visits).hasSize(2);
		Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
		assertThat(visitArr[0].getPet()).isNotNull();
		assertThat(visitArr[0].getDate()).isNotNull();
		assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
	}
}
