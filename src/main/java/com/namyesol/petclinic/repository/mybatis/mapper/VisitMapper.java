package com.namyesol.petclinic.repository.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namyesol.petclinic.domain.Visit;

@Mapper
public interface VisitMapper {

	void insertVisit(Visit visit);
	
	List<Visit> findVisitsByPetId(int petId);
}
