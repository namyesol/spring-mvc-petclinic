package com.namyesol.petclinic.repository.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Visit;
import com.namyesol.petclinic.repository.VisitRepository;

@Repository
public class JdbcVisitRepository implements VisitRepository{

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private SimpleJdbcInsert insertVisit;
	
	@Autowired
	public JdbcVisitRepository(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.insertVisit = new SimpleJdbcInsert(dataSource).withTableName("visits");
	}
	
	@Override
	public void save(Visit visit) {
		if (visit.isNew()) {
			visit.setId(nextId());
			this.insertVisit.execute(createVisitParameterSource(visit));
		} else {
			throw new UnsupportedOperationException("Visit update not supported");
		}
	}

	private Integer nextId() {
		return this.jdbcTemplate.queryForObject("SELECT visits_id_seq.nextval FROM DUAL", EmptySqlParameterSource.INSTANCE, Integer.class);
	}

	private MapSqlParameterSource createVisitParameterSource(Visit visit) {
		return new MapSqlParameterSource()
				.addValue("id", visit.getId())
				.addValue("pet_id", visit.getPet().getId())
				.addValue("visit_date", Date.valueOf(visit.getDate()))
				.addValue("description", visit.getDescription());
	}
	
	@Override
	public List<Visit> findByPetId(Integer petId) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", petId);
		JdbcPet pet = this.jdbcTemplate.queryForObject(
				"SELECT id, name, birth_date, type_id, owner_id FROM pets WHERE id=:id",
				params,
				new RowMapper<JdbcPet>() {
					@Override
					public JdbcPet mapRow(ResultSet rs, int rowNum) throws SQLException {
						JdbcPet pet = new JdbcPet();
						pet.setId(rs.getInt("id"));
						pet.setName(rs.getString("name"));
						pet.setBirthDate(rs.getObject("birth_date", Date.class).toLocalDate());
						pet.setTypeId(rs.getInt("type_id"));
						pet.setOwnerId(rs.getInt("owner_id"));
						return pet;
					}
				});
		
		List<Visit> visits = this.jdbcTemplate.query(
				"SELECT id as visit_id, visit_date, description FROM visits WHERE pet_id=:id",
				params,
				new JdbcVisitRowMapper());
		
		for (Visit visit : visits) {
			visit.setPet(pet);
		}
		
		return visits;
	}

}
