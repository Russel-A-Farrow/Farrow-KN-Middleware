package com.farrow.knmiddleware.daos;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class AbstractFssbfdDaoJdbc {


	protected DataSource dataSource;
	
	protected NamedParameterJdbcTemplate jdbcTemplate = null;

	@Autowired
	protected void setDataSource (DataSource ds){
		this.dataSource=ds;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
}
