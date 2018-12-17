package com.sbk.ssample;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class DBConfig {
	
	
	// 데이터소스를 만들기 위해 커넥션풀 프로퍼티 객체를 반환하는 빈을 선언합니다.
    // 해당 빈을 만들 때 @ConfigurationProperties 어노테이션을 사용합니다.
    // "prefix" 값을 이용해 해당 prefix로 시작하는 값들을 주입 받습니다.
	@Bean
	@ConfigurationProperties("spring.datasource")
	public HikariDataSource dataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

    
    //트랜잭션 매니저를 반환하는 빈을 선언합니다.
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    	JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(emf);
		return tm;
    }
    
    @Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());			
		em.setPackagesToScan("com.sbk.ssample");	// 호출하지 않는 경우 resource 디렉토리에서 persistent.xml 을 찾는다. 
		
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(jpaProperties());
		
		return em;
	}
    
    /*
	 * 하이버 네이트 프로퍼티를 설정한다. 
	 */
	Properties jpaProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "create");			// 테이블 자동생성
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");   // INNODB 
		properties.setProperty("hibernate.show_sql", "true");						// SQL show
		properties.setProperty("hibernate.format_sql", "true");						// SQL formating
		return properties;
	}
}
