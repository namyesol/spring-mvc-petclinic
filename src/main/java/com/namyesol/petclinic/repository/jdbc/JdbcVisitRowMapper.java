package com.namyesol.petclinic.repository.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.namyesol.petclinic.domain.Visit;

public class JdbcVisitRowMapper implements RowMapper<Visit>{

	@Override
	public Visit mapRow(ResultSet rs, int rowNum) throws SQLException {
		Visit visit = new Visit();
		visit.setId(rs.getInt("visit_id"));
		visit.setDate(rs.getObject("visit_date", Date.class).toLocalDate());
		visit.setDescription(rs.getString("description"));
		return visit;
	}

}
