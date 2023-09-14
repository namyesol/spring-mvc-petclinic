package com.namyesol.petclinic.repository;

import java.util.List;

import com.namyesol.petclinic.domain.Visit;

public interface VisitRepository {
	
	/**
     * Save a <code>Visit</code> to the data store, either inserting or updating it.
     *
     * @param visit the <code>Visit</code> to save
     * @see BaseEntity#isNew
     */
	void save(Visit visit);
	
	List<Visit> findByPetId(Integer petId);
}
