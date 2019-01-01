package com.sbk.ssample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraCqlSessionFactoryBean;

@Configuration
public class Cassandra {
	
	  @Value("${spring.data.cassandra.contact-points}") 
	  String contactPoints;
	 
	  /*
	   * Factory bean that creates the com.datastax.driver.core.Session instance
	   */ 
	  @Bean
	  public CassandraCqlClusterFactoryBean cluster() {

	    CassandraCqlClusterFactoryBean cluster = new CassandraCqlClusterFactoryBean();
	    cluster.setContactPoints(contactPoints);
	    cluster.setJmxReportingEnabled(false);        /* java.lang.NoClassDefFoundError: com/codahale/metrics/JmxReporter 예외 해결 */
	    System.err.println("cassandra contactPoints:" + contactPoints);
	    
	    return cluster;
	  }

	   /*
	    * Factory bean that creates the com.datastax.driver.core.Session instance
	    */
	   @Bean
	   public CassandraCqlSessionFactoryBean session() {

	    CassandraCqlSessionFactoryBean session = new CassandraCqlSessionFactoryBean();
	    session.setCluster(cluster().getObject());
	    
	    session.setKeyspaceName("sample");

	    return session;
	  }
	
}
