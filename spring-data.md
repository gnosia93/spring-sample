## MySQL ##
```
SQL> create database sample;
SQL> create user 'sample'@'%' identified by 'sample';
SQL> grant all privileges on sample.* to 'sample'@'%';
SQL> status
```

## Spring Boot JPA Sample ##

#### application.properties ####
```
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.jdbcUrl=jdbc:mysql://localhost:3306/sample      # hikari datasource 용 URL
spring.datasource.url=jdbc:mysql://localhost:3306/sample          # @DataJpaTest 용 URL
spring.datasource.username=sample
spring.datasource.password=sample
```

#### Entity Manager -> TX Manager -> DataSource 설정 ####
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
### JUnit Test ###
```
package io.startup.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.startup.demo.entity.Address;
import io.startup.demo.entity.Email;
import io.startup.demo.entity.Gender;
import io.startup.demo.entity.Member;
import io.startup.demo.entity.repository.MemberRepository;

@RunWith(SpringRunner.class)
@DataJpaTest											// Jpa test
@AutoConfigureTestDatabase(replace=Replace.NONE)		// replace h2 to mysql
public class JpaRepositoryTest {

	@Autowired MemberRepository memberRepository;
	
	@Before
	public void before() {
		System.err.println("before() " + memberRepository.count());
	}

	@After
	public void after() {
		System.err.println("after() " + memberRepository.count());
	}
	
	@BeforeTransaction
	public void beforeTx() {
		System.err.println("beforeTx() " + memberRepository.count());
	}
	
	@AfterTransaction
	public void afterTx() {
		System.err.println("afterTx() " + memberRepository.count());
	}
	
	
	@Test
	public void saveAndFind( ) {
		Member member = new Member("test_name", 
					Gender.MALE, 
					new Email("gnosia@naver.com"), 
					new Address("addr1", "addr2", "00000"));
		
		memberRepository.save(member);
		assertThat(memberRepository.getOne(member.getMemberId()), equalTo(member));
	}
}



```

## Entities ##
```
package io.startup.demo.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name="tb_member")
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="memb_id")
	long memberId;
	
	@NonNull
	@Column(name="name", nullable=false, length=100)
	String name;
	
	@NonNull
	@Enumerated(value=EnumType.STRING)
	@Column(name="sex", nullable=false, length=10)
	Gender gender;
	
	@NonNull
	@Embedded
	Email email;
	
	@NonNull
	@Embedded
	Address address;
}


package io.startup.demo.entity;

import lombok.Data;

public enum Gender {
	MALE("M"), FEMAIL("F"), UNKNOWN("NA");
	
	String code;
	
	private Gender(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
}

package io.startup.demo.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable					// 다른 객체에 포함되는 경우 설정.
public class Email {
	@Column(length=30)
	String email;
	
}


package io.startup.demo.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class Address {
	@Column(length=100)
	String addr1;
	
	@Column(length=200)
	String addr2;
	
	@Column(name="zip_code", length=5, nullable=false)
	String zipCode;
}


```













JPA Reference.

https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa

https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.namespace
