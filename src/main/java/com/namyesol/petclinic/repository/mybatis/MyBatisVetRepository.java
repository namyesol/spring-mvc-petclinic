package com.namyesol.petclinic.repository.mybatis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Vet;
import com.namyesol.petclinic.repository.VetRepository;
import com.namyesol.petclinic.repository.mybatis.mapper.VetMapper;

@Repository
public class MyBatisVetRepository implements VetRepository {

	private VetMapper vetMapper;
	
	@Autowired
	public MyBatisVetRepository(VetMapper vetMapper) {
		this.vetMapper = vetMapper;
	}
	
	@Override
	public Collection<Vet> findAll() {		
		return vetMapper.findAll();
	}

}
