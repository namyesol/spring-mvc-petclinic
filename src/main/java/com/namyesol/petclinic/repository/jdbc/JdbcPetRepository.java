package com.namyesol.petclinic.repository.jdbc;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.repository.OwnerRepository;
import com.namyesol.petclinic.repository.PetRepository;

@Repository
public class JdbcPetRepository implements PetRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private SimpleJdbcInsert insertPet;
	
	private OwnerRepository ownerRepository;
	
	@Autowired
	public JdbcPetRepository(DataSource dataSource, OwnerRepository ownerRepository) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		this.insertPet = new SimpleJdbcInsert(dataSource).withTableName("pets");
		
		this.ownerRepository = ownerRepository;
	}
	
	@Override
	public List<PetType> findPetTypes() {
		return this.namedParameterJdbcTemplate.query(
			"SELECT id, name FROM types ORDER BY name",
			EmptySqlParameterSource.INSTANCE,
			BeanPropertyRowMapper.newInstance(PetType.class));
	}

	@Override
	public Pet findById(int id) {
		Integer ownerId;
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			ownerId = this.namedParameterJdbcTemplate.queryForObject(
					"SELECT owner_id FROM pets WHERE id=:id",
					params,
					Integer.class);
		} catch (EmptyResultDataAccessException ex) {
			throw new ObjectRetrievalFailureException(Pet.class, id);
		}
		Owner owner = this.ownerRepository.findById(ownerId);

		return owner.getPets().stream().filter(pet -> pet.getId().equals(id)).findAny().orElse(null);
	}

	@Override
	public void save(Pet pet) {
		if (pet.isNew()) {
			pet.setId(nextId());
			this.insertPet.execute(createPetParameterSource(pet));
		} else {
			this.namedParameterJdbcTemplate.update(
				"UPDATE pets SET name=:name, birth_date=:birth_date, type_id=:type_id, " +
				"owner_id=:owner_id WHERE id=:id",
				createPetParameterSource(pet));
		}
	}
	
	private MapSqlParameterSource createPetParameterSource(Pet pet) {
		return new MapSqlParameterSource()
				.addValue("id", pet.getId())
				.addValue("name", pet.getName())
				.addValue("birth_date", Date.valueOf(pet.getBirthDate()))
				.addValue("type_id", pet.getType().getId())
				.addValue("owner_id", pet.getOwner().getId());
	}
	
	private Integer nextId() {
		return this.namedParameterJdbcTemplate.queryForObject(
				"SELECT pets_id_seq.nextval FROM DUAL", 
				EmptySqlParameterSource.INSTANCE,
				Integer.class);
	}
	
}
