package com.namyesol.petclinic.repository.jdbc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.repository.OwnerRepository;

@Repository
public class JdbcOwnerRepository implements OwnerRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private SimpleJdbcInsert insertOwner;
	
	@Autowired
	public JdbcOwnerRepository(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		this.insertOwner = new SimpleJdbcInsert(dataSource).withTableName("owners");
	}
	
	@Override
	public Collection<Owner> findByLastName(String lastName) {
		Map<String, Object> params = new HashMap<>();
		params.put("lastName", lastName + "%");
		List<Owner> owners = this.namedParameterJdbcTemplate.query(
				"SELECT id, first_name, last_name, address, city, telephone FROM owners WHERE last_name like :lastName",
				params,
				BeanPropertyRowMapper.newInstance(Owner.class));
		
		loadOwnersPetsAndVisits(owners);
		return owners;
	}

	@Override
	public Owner findById(int id) {
		Owner owner;
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			owner = this.namedParameterJdbcTemplate.queryForObject(
					"SELECT id, first_name, last_name, address, city, telephone FROM owners WHERE id= :id",
					params,
					BeanPropertyRowMapper.newInstance(Owner.class)
			);
		} catch (EmptyResultDataAccessException ex) {
			throw new ObjectRetrievalFailureException(Owner.class, id);
		}
		loadPetsAndVisits(owner);
		return owner;
	}

	private void loadOwnersPetsAndVisits(List<Owner> owners) {
		for (Owner owner : owners) {
			loadPetsAndVisits(owner);
		}
	}
	
	private void loadPetsAndVisits(Owner owner) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", owner.getId());
		List<JdbcPet> pets = this.namedParameterJdbcTemplate.query(
				"SELECT p.id as pet_id, p.name as pet_name, p.birth_date as pet_birth_date, p.type_id as pet_type_id, p.owner_id as pet_owner_id, v.id as visit_id, v.visit_date as visit_date, v.description as visit_description, v.pet_id as visit_pet_id FROM pets p LEFT OUTER JOIN visits v ON p.id = v.pet_id WHERE p.owner_id=:id ORDER BY visit_pet_id",
				Collections.singletonMap("id", owner.getId()),
				new JdbcPetVisitExtractor()
		);
		Collection<PetType> petTypes = getPetTypes();
		for (JdbcPet pet : pets) {
			PetType petType = petTypes.stream().filter(type -> type.getId().equals(pet.getTypeId())).findAny().get();
			pet.setType(petType);
			owner.addPet(pet);
		}
	}
	
	public Collection<PetType> getPetTypes() {
		return this.namedParameterJdbcTemplate.query(
				"SELECT id, name FROM types ORDER BY name",
				new HashMap<String, Object>(),
				BeanPropertyRowMapper.newInstance(PetType.class));
	}
	
	@Override
	public void save(Owner owner) {
		if (owner.isNew()) {
			owner.setId(nextId());
			BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(owner);
			this.insertOwner.execute(parameterSource);
		} else {
			BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(owner);
			this.namedParameterJdbcTemplate.update(
					"UPDATE owners SET first_name=:firstName, last_name=:lastName, address=:address, " +
					"city=:city, telephone=:telephone WHERE id=:id",
					parameterSource);
		}
	}
	
	private Integer nextId() {
		return this.namedParameterJdbcTemplate.queryForObject("SELECT owners_id_seq.nextval FROM DUAL", EmptySqlParameterSource.INSTANCE, Integer.class);
	}
	
}
