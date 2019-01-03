package com.sbk.ssample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;

// @Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {
	
	@Autowired MongoClient mongoClient;
	
	@Override
	public void run(String... args) throws Exception {
		class Person {

			  private String id;
			  private String name;
			  private int age;

			  public Person(String name, int age) {
			    this.name = name;
			    this.age = age;
			  }

			  public String getId() {
			    return id;
			  }
			  public String getName() {
			    return name;
			  }
			  public int getAge() {
			    return age;
			  }

			  @Override
			  public String toString() {
			    return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
			  }
			}

		MongoOperations mongoOps = new MongoTemplate(mongoClient, "database");
	    mongoOps.insert(new Person("Joe", 34));

	}
	

}
