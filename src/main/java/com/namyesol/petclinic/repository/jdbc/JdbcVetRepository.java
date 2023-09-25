package com.namyesol.petclinic.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Specialty;
import com.namyesol.petclinic.domain.Vet;
import com.namyesol.petclinic.repository.VetRepository;

@Repository
public class JdbcVetRepository implements VetRepository{

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JdbcVetRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Collection<Vet> findAll() {
		List<Vet> vets = new ArrayList<>();
		
		vets.addAll(this.jdbcTemplate.query(
				"SELECT id, first_name, last_name FROM vets ORDER BY last_name,first_name",
				BeanPropertyRowMapper.newInstance(Vet.class)));
		
		final List<Specialty> specialties= this.jdbcTemplate.query(
				"SELECT id, name FROM specialties",
				BeanPropertyRowMapper.newInstance(Specialty.class));
		
		for (Vet vet: vets) {
			final List<Integer> vetSpecialtiesIds = this.jdbcTemplate.query(
					"SELECT specialty_id FROM vet_specialties WHERE vet_id=?",
					new BeanPropertyRowMapper<Integer>() {
						@Override
						public Integer mapRow(ResultSet rs, int row) throws SQLException {
							return rs.getInt(1);
						}
					}, vet.getId());
			for (int specialtyId : vetSpecialtiesIds) {
				Specialty specialty = specialties.stream().filter(s -> s.getId().equals(specialtyId)).findAny().get();
				vet.addSpecialty(specialty);
			}
		}
		return vets;
	}
}
