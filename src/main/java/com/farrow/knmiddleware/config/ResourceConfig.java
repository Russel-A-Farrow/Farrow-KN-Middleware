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
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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
		ds.setMaximumPoolSize(20);
		ds.setLeakDetectionThreshold(MINUTES.toMillis(4));
		ds.setMaxLifetime(MINUTES.toMillis(5));
		return ds;
	}

	
	@Bean(name = "entityManager")
	@Primary
	EntityManager farrowReaderEntityManagerFssbfd() {
		return farrowEntityManagerFactoryFssbfd().createEntityManager();
	}

	@Bean(name = "entityManagerFactory")
	@Primary
	EntityManagerFactory farrowEntityManagerFactoryFssbfd() {
		LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
		lef.setDataSource(dataSource());
		lef.setJpaVendorAdapter(farrowReaderJpaVendorAdapterFssbfd());
		lef.setPackagesToScan("com.farrow.imagingapi.*");
		lef.setPersistenceUnitName("FSSBFD");
		lef.afterPropertiesSet();
		return lef.getObject();
	}

	@Bean(name = "transactionManager")
	@Primary
	PlatformTransactionManager farrowReaderTransactionManagerCdn() {
		return new JpaTransactionManager(farrowEntityManagerFactoryFssbfd());
	}

	public JpaVendorAdapter farrowReaderJpaVendorAdapterFssbfd() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(false);
		jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.DB2Dialect");
		jpaVendorAdapter.setDatabase(Database.DB2);
		return jpaVendorAdapter;
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
