package com.namyesol.petclinic.repository.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namyesol.petclinic.domain.Owner;

@Mapper
public interface OwnerMapper {

	void insertOwner(Owner owner);

	Owner findOwnerById(int id);

	List<Owner> findOwnersByLastName(String lastName);
	
	void updateOwner(Owner owner);

}
