package com.namyesol.petclinic.repository.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;

@Mapper
public interface PetMapper {

	List<PetType> findPetTypes();
	
	void insertPet(Pet pet);
	
	Pet findPetById(int id);
	
	List<Pet> findPetsByOwnerId(int ownerId);
	
	void updatePet(Pet pet);
	
	Integer findOwnerIdByPetId(int petId);
}
