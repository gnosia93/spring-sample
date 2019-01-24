## 오류 케이스 ##

1. POM 에서 data-jpa 만 추가한 경우 아래와 같은 예외 발생

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).
```

2. POM 에 H2 의존성 추가

@Entity 테이블을 자동으로 생성해 준다. 

```
#pom
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>   

#application.properties
spring.jpa.show-sql=true


#code
@SpringBootApplication
public class SpringJpgNativeQuery1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpgNativeQuery1Application.class, args);
	}
	
	@NoArgsConstructor
	@Data
	@Entity
	@Table(name="tb_user")
	class User {
		@Id
		long id;
		String name;
		int status;
	}

}

#log
Hibernate: drop table tb_user if exists
Hibernate: create table tb_user (id bigint not null, name varchar(255), status integer not null, primary key (id))

```



## sample code ##

```

```

## 레퍼런스 ##

https://thoughts-on-java.org/jpa-native-queries/
