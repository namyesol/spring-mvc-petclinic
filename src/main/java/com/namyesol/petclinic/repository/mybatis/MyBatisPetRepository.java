package com.namyesol.petclinic.repository.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.repository.PetRepository;

@Repository
public class MyBatisPetRepository implements PetRepository {

	@Override
	public List<PetType> findPetTypes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pet findById(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(Pet pet) {
		throw new UnsupportedOperationException();
	}
	
}
