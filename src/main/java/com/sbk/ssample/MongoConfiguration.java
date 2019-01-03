package com.sbk.ssample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


// @Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {
	
	/*
	 *  1. external 설정 사용하는 방법 ?
	 *  2. mongodb 권한 관리. 
	 */

	
	@Value("${spring.data.mongodb.host}") 
	String mongoHost;
	
	
	@Override
	@Bean
	public MongoClient mongoClient() {
		
		return MongoClients.create("mongodb://sample:sample@" + mongoHost + ":27017/sample");
	}

	@Override
	protected String getDatabaseName() {
		return "sample";
	}

}
