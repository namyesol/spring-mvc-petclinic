package com.namyesol.petclinic.repository;

import java.util.Collection;

import com.namyesol.petclinic.domain.Vet;

public interface VetRepository {
	
    /**
     * Retrieve all <code>Vet</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>Vet</code>s
     */
	Collection<Vet> findAll();
}
