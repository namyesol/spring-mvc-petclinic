package com.namyesol.petclinic.repository.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.namyesol.petclinic.domain.Visit;

public class JdbcPetVisitExtractor extends OneToManyResultSetExtractor<JdbcPet, Visit, Integer> {

	public JdbcPetVisitExtractor() {
		super(new PetMapper(), new VisitMapper());
	}

	@Override
	protected Integer mapPrimaryKey(ResultSet rs) throws SQLException {
		return rs.getInt("pet_id");
	}

	@Override
	protected Integer mapForeignKey(ResultSet rs) throws SQLException {
		if (rs.getObject("visit_pet_id") == null) {
			return null;
		}
		else {
			return rs.getInt("visit_pet_id");
		}
	}

	@Override
	protected void addChild(JdbcPet root, Visit child) {
		root.addVisit(child);
	}

	private static class PetMapper implements RowMapper<JdbcPet> {

		@Override
		public JdbcPet mapRow(ResultSet rs, int rowNum) throws SQLException {
			JdbcPet pet = new JdbcPet();
			pet.setId(rs.getInt("pet_id"));
			pet.setName(rs.getString("pet_name"));
			pet.setBirthDate(rs.getObject("pet_birth_date", Date.class).toLocalDate());
			pet.setOwnerId(rs.getInt("pet_owner_id"));
			pet.setTypeId(rs.getInt("pet_type_id"));
			return pet;
		}	
	}
	
	private static class VisitMapper implements RowMapper<Visit> {

		@Override
		public Visit mapRow(ResultSet rs, int rowNum) throws SQLException {
			Visit visit = new Visit();
			visit.setId(rs.getInt("visit_id"));
			visit.setDate(rs.getObject("visit_date", Date.class).toLocalDate());
			visit.setDescription(rs.getString("visit_description"));
			return visit;
		}
		
	}
}
