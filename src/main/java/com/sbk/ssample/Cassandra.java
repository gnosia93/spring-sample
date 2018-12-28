package com.sbk.ssample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraCqlSessionFactoryBean;

@Configuration
@ConfigurationProperties(prefix="spring.data.cassandra")
public class Cassandra {
	
	 @Value("contact-points") String contactPoints;
	
	  /*
	   * Factory bean that creates the com.datastax.driver.core.Session instance
	   */ 
	  @Bean
	  public CassandraCqlClusterFactoryBean cluster() {

	    CassandraCqlClusterFactoryBean cluster = new CassandraCqlClusterFactoryBean();
	    cluster.setContactPoints("192.168.29.191");
	    //cluster.setJmxReportingEnabled(false);
	    System.err.println("cassandra:" + contactPoints);
	    
	    return cluster;
	  }

	   /*
	    * Factory bean that creates the com.datastax.driver.core.Session instance
	    */
	   @Bean
	   public CassandraCqlSessionFactoryBean session() {

	    CassandraCqlSessionFactoryBean session = new CassandraCqlSessionFactoryBean();
	    session.setCluster(cluster().getObject());
	    
	    session.setKeyspaceName("mykeyspace");

	    return session;
	  }
	
}
