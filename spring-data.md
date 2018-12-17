## MySQL ##
```
SQL> create database sample;
SQL> create user 'sample'@'%' identified by 'sample';
SQL> grant all privileges on sample.* to 'sample'@'%';
SQL> status
```

## Spring Boot JPA Sample ##
```
package io.startup.demo;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement            // JPA 트랜잭션 관리를 위한 CGLIB 형태의  프록시 모드 설정
public class DBConfig {

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());			
		em.setPackagesToScan("io.startup.demo");	// 호출하지 않는 경우 resource 디렉토리에서 persistent.xml 을 찾는다. 
		
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(jpaProperties());
		
		return em;
	}
	
	@Bean
	public PlatformTransactionManager transactionManger(EntityManagerFactory emf) {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(emf);
		return tm;
	}
	
	/*
	 * 커넥션 풀링을 지원하지 않는 심플 데이터 소스를 리턴한다.
	// @Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/sample");
		dataSource.setUsername("sample");
		dataSource.setPassword("sample");
		return dataSource;
	}
	*/
	
	/*
	 * Hikari CP 를 사용하도록 수정, application.properties 의 spring.datasoruce prefix 사용 
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create()
					.type(HikariDataSource.class)
					.build();
	}
	
	/*
	 * 하이버 네이트 프로퍼티를 설정한다. 
	 */
	Properties jpaProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");			// 테이블 자동생성
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");   // INNODB 
		properties.setProperty("hibernate.show_sql", "true");						// SQL show
		properties.setProperty("hibernate.format_sql", "true");						// SQL formating
		return properties;
	}
}
```















JPA Reference.

https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa

https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.namespace
