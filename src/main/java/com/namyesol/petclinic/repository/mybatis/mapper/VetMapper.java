package com.namyesol.petclinic.repository.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namyesol.petclinic.domain.Vet;

@Mapper
public interface VetMapper {

	List<Vet> findAll();
}
