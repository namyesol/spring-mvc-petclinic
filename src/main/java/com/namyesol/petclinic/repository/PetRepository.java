package com.namyesol.petclinic.repository;

import java.util.List;

import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;

public interface PetRepository {
	
	/**
     * Retrieve all <code>PetType</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>PetType</code>s
     */
	List<PetType> findPetTypes();
    
	/**
     * Retrieve a <code>Pet</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>Pet</code> if found
     * @throws org.springframework.dao.DataRetrievalFailureException if not found
     */
	Pet findById(int id);
	
	/**
     * Save a <code>Pet</code> to the data store, either inserting or updating it.
     *
     * @param pet the <code>Pet</code> to save
     * @see BaseEntity#isNew
     */
	void save(Pet pet);
}
