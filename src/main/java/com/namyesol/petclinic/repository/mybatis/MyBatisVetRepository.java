package com.namyesol.petclinic.repository.mybatis;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Vet;
import com.namyesol.petclinic.repository.VetRepository;

@Repository
public class MyBatisVetRepository implements VetRepository {

	@Override
	public Collection<Vet> findAll() {
		throw new UnsupportedOperationException();
	}

}
