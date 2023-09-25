package com.namyesol.petclinic.repository.jdbc;

import com.namyesol.petclinic.domain.Pet;

/**
 * Subclass of Pet that carries temporary id properties which are only relevant for a JDBC implementation of the
 * PetRepository.
 */
public class JdbcPet extends Pet {
	
	private int typeId;
	
	private int ownerId;

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
}
