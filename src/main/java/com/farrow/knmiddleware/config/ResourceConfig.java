package com.farrow.knmiddleware.config;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariDataSource;

import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;

@Configuration
public class ResourceConfig {
	@Autowired
	private Environment env;
	
	@Bean("dataSourceFssbfd")
	DataSource dataSource() {
		DataSourceBuilder<HikariDataSource> dataSourceBuilder = DataSourceBuilder.create().type(HikariDataSource.class);
		dataSourceBuilder.driverClassName("com.ibm.as400.access.AS400JDBCDriver");
		dataSourceBuilder.url(env.getProperty("as400db.url"));
		dataSourceBuilder.username(env.getProperty("as400db.userid"));
		dataSourceBuilder.password(env.getProperty("as400db.password"));
		
		HikariDataSource ds = dataSourceBuilder.build();
		ds.setConnectionTestQuery("VALUES 1");
		ds.setMaximumPoolSize(50);
		ds.setLeakDetectionThreshold(MINUTES.toMillis(4));
		ds.setMaxLifetime(MINUTES.toMillis(5));
		return ds;
	}

	@Bean("knadapterShedLockProvider")
	LockProvider lockProvider() {
		return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
				.withJdbcTemplate(new JdbcTemplate(dataSource())).usingDbTime().build());
	}

	@Bean("knadapterLockingTaskExecutor")
	LockingTaskExecutor lockingTaskExecutor() {
		return new DefaultLockingTaskExecutor(lockProvider());
	}
	
	@Bean
	ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
        mapper.registerModule(new JavaTimeModule());
        mapper.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
        mapper.setDateFormat(dateFormat);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}
}
