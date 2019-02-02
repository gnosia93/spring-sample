## POM ##
```
<dependencies>

  ...
	<!-- H2 Database -->
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
		</dependency>

  ...

</dependencies>

```


## application.properties ##
```
# H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2
# Datasource
spring.datasource.url=jdbc:h2:file:~/test
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
```

## DB Console 접속 ##

http://localhost:8080/h2




## 레퍼런스 ##

https://dzone.com/articles/integrate-h2-database-in-your-spring-boot-applicat
