package com.namyesol.petclinic.repository.mybatis;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.repository.OwnerRepository;

@Repository
public class MyBatisOwnerRepository implements OwnerRepository {

	@Override
	public Collection<Owner> findByLastName(String lastName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Owner findById(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(Owner owner) {
		throw new UnsupportedOperationException();
	}

}
