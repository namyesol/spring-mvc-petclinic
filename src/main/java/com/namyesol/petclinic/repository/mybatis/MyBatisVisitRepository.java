package com.namyesol.petclinic.repository.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Visit;
import com.namyesol.petclinic.repository.VisitRepository;

@Repository
public class MyBatisVisitRepository implements VisitRepository {

	@Override
	public void save(Visit visit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Visit> findByPetId(Integer petId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
