package com.namyesol.petclinic.repository.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class JdbcPetRowMapper implements RowMapper<JdbcPet>{
	
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
	
}
