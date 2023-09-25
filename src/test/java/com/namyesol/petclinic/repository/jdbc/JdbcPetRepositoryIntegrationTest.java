package com.namyesol.petclinic.repository.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.domain.Visit;

public class JdbcPetRepositoryIntegrationTest extends JdbcRepositoryIntegrationTest{
	
	@Autowired
	JdbcPetRepository petRepository;
	
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	
	@Test
	public void shouldFindPetTypes() {
		List<PetType> petTypes = petRepository.findPetTypes();
		PetType petType1 = petTypes.stream().filter(petType -> petType.getId().equals(1)).findAny().get();
		assertThat(petType1.getName()).isEqualTo("cat");
		PetType petType4 = petTypes.stream().filter(petType -> petType.getId().equals(4)).findAny().get();
		assertThat(petType4.getName()).isEqualTo("snake");
	}
	
	@Test
	public void testJdbcPetRowMapper() {
		JdbcPet pet7 = this.jdbcTemplate.queryForObject("SELECT pets.id, name, birth_date, type_id, owner_id FROM pets WHERE id= :id",
				Collections.singletonMap("id", 7),
				new JdbcPetRowMapper());
		assertThat(pet7.getId()).isEqualTo(7);
		assertThat(pet7.getName()).startsWith("Samantha");
		assertThat(pet7.getBirthDate()).isEqualTo("1995-09-04");
		assertThat(pet7.getTypeId()).isEqualTo(1);
		assertThat(pet7.getOwnerId()).isEqualTo(6);
	}
	
	@Test
	public void testJdbcVisitRowMapper() {
		List<Visit> visits = this.jdbcTemplate.query(
				"SELECT id as visit_id, visit_date, description FROM visits WHERE pet_id=:id",
				Collections.singletonMap("id", 7),
				new JdbcVisitRowMapper());
		
		List<Integer> visitIds = visits.stream().map(visit -> visit.getId()).collect(Collectors.toList());
		assertThat(visitIds).contains(1, 4);
		
	}
	
	@Test
	public void testJdbcPetVisitExtractor() {
		Integer ownerId = this.jdbcTemplate.queryForObject(
				"SELECT owner_id FROM pets WHERE id=:id",
				Collections.singletonMap("id", 7),
				Integer.class);
		assertThat(ownerId).isEqualTo(6);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", ownerId);
		List<JdbcPet> pets = this.jdbcTemplate.query(
				"SELECT p.id as pet_id, p.name as pet_name, p.birth_date as pet_birth_date, p.type_id as pet_type_id, p.owner_id as pet_owner_id, v.id as visit_id, v.visit_date as visit_date, v.description as visit_description, v.pet_id as visit_pet_id FROM pets p LEFT OUTER JOIN visits v ON p.id = v.pet_id WHERE p.owner_id=:id ORDER BY visit_pet_id",
				params,
				new JdbcPetVisitExtractor());
		JdbcPet pet7 = pets.stream().filter(pet -> pet.getId().equals(7)).findAny().get();
		assertThat(pet7.getId()).isEqualTo(7);
		assertThat(pet7.getName()).startsWith("Samantha");
		assertThat(pet7.getBirthDate()).isEqualTo("1995-09-04");
		assertThat(pet7.getTypeId()).isEqualTo(1);
		assertThat(pet7.getOwnerId()).isEqualTo(6);
		assertThat(pet7.getVisits()).hasSize(2);
		List<Integer> visitIds = pet7.getVisits().stream().map(visit -> visit.getId()).collect(Collectors.toList());
		assertThat(visitIds).contains(1, 4);
	}
	
}

