package com.namyesol.petclinic.repository.mybatis.type;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setObject(i, Date.valueOf(parameter));
	}

	@Override
	public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {		
		return rs.getObject(columnName, Date.class).toLocalDate();
	}

	@Override
	public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getObject(columnIndex, Date.class).toLocalDate();
	}

	@Override
	public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getObject(columnIndex, Date.class).toLocalDate();
	}

}
