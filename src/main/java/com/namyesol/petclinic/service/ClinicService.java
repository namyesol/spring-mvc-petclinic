package com.namyesol.petclinic.service;

import java.util.Collection;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.domain.Vet;
import com.namyesol.petclinic.domain.Visit;

public interface ClinicService {
	
	Collection<PetType> findPetTypes();
	
	Owner findOwnerById(int id);

	Pet findPetById(int id);
	
	void savePet(Pet pet);
	
	void saveVisit(Visit visit);
	
	Collection<Vet> findVets();
	
	void saveOwner(Owner owner);
	
	Collection<Owner> findOwnerByLastName(String lastName);
	
	Collection<Visit> findVisitsByPetId(int petId);
}
