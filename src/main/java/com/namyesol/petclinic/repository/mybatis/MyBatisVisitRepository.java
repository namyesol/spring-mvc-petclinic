package com.namyesol.petclinic.repository.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.Visit;
import com.namyesol.petclinic.repository.VisitRepository;
import com.namyesol.petclinic.repository.mybatis.mapper.PetMapper;
import com.namyesol.petclinic.repository.mybatis.mapper.VisitMapper;

@Repository
public class MyBatisVisitRepository implements VisitRepository {

	private final VisitMapper visitMapper;
	private final PetMapper petMapper;
	
	@Autowired
	public MyBatisVisitRepository(VisitMapper visitMapper, PetMapper petMapper) {
		this.visitMapper = visitMapper;
		this.petMapper = petMapper;
	}
	
	
	@Override
	public void save(Visit visit) {
		if (visit.isNew()) {
			visitMapper.insertVisit(visit);
		} else {
			throw new UnsupportedOperationException("Visit update not supported");
		}
	}

	@Override
	public List<Visit> findByPetId(Integer petId) {
		Pet pet = petMapper.findPetById(petId);
		List<Visit> visits = visitMapper.findVisitsByPetId(petId);
		
		for (Visit visit : visits) {
			visit.setPet(pet);
		}
		
		return visits;
	}
	
}
