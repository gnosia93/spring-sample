package com.sbk.ssample;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
// @EnableTransactionManagement
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
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource());
        return manager;
    }
}
