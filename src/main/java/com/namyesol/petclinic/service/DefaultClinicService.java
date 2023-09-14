package com.namyesol.petclinic.service;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.domain.Vet;
import com.namyesol.petclinic.domain.Visit;
import com.namyesol.petclinic.repository.OwnerRepository;
import com.namyesol.petclinic.repository.PetRepository;
import com.namyesol.petclinic.repository.VetRepository;
import com.namyesol.petclinic.repository.VisitRepository;

/**
 * Mostly used as a facade for all Petclinic controllers
 * Also a placeholder for @Transactional and @Cacheable annotations
 */
@Service
public class DefaultClinicService implements ClinicService {

	private final PetRepository petRepository;
	private final VetRepository vetRepository;
	private final OwnerRepository ownerRepository;
	private final VisitRepository visitRepository;
	
	public DefaultClinicService(PetRepository petRepository, VetRepository vetRepository, OwnerRepository ownerRepository, VisitRepository visitRepository) {
		this.petRepository = petRepository;
		this.vetRepository = vetRepository;
		this.ownerRepository = ownerRepository;
		this.visitRepository = visitRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() {
		return petRepository.findPetTypes();
	}

	@Override
	@Transactional(readOnly = true)
	public Owner findOwnerById(int id) {
		return ownerRepository.findById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Collection<Owner> findOwnerByLastName(String lastName) {
		return ownerRepository.findByLastName(lastName);
	}
	
	@Override
	@Transactional
	public void saveOwner(Owner owner) {
		ownerRepository.save(owner);
	}

	@Override
	@Transactional(readOnly = true)
	public Pet findPetById(int id) {
		return petRepository.findById(id);
	}

	@Override
	@Transactional
	public void savePet(Pet pet) {
		petRepository.save(pet);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}
	
	@Override
	public void saveVisit(Visit visit) {
		visitRepository.save(visit);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Collection<Vet> findVets() {
		return vetRepository.findAll();
	}

}
