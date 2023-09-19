package com.namyesol.petclinic.repository.mybatis;

import java.util.List;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.repository.OwnerRepository;
import com.namyesol.petclinic.repository.PetRepository;
import com.namyesol.petclinic.repository.mybatis.mapper.PetMapper;

@Repository
public class MyBatisPetRepository implements PetRepository {

	private final PetMapper petMapper;

	private final OwnerRepository ownerRepository;
	
	public MyBatisPetRepository(PetMapper petMapper, OwnerRepository ownerRepository) {
		this.petMapper = petMapper;
		this.ownerRepository = ownerRepository;
	}
	
	@Override
	public List<PetType> findPetTypes() {
		return petMapper.findPetTypes();
	}

	@Override
	public Pet findById(int id) {
		Integer ownerId = petMapper.findOwnerIdByPetId(id);
		if (ownerId == null) {
			throw new ObjectRetrievalFailureException(Pet.class, id);
		}
		
		Owner owner = ownerRepository.findById(ownerId);
		return owner.getPets().stream().filter(pet -> pet.getId().equals(id)).findAny().get();
	}

	@Override
	public void save(Pet pet) {
		if (pet.isNew()) {
			petMapper.insertPet(pet);
		} else {
			petMapper.updatePet(pet);
		}
	}
}
