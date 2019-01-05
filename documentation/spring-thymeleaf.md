##  POM ##

```
<dependency>
	<groupId>org.springframework.boot</groupId>
 	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency> 
```    
```
Using Thymeleaf
If Spring Boot scans Thymeleaf library in its classpath then we are ready to work with Thymeleaf. Spring Boot provides properties for Thymeleaf that will be configured in application.properties or application.yml to change the configurations of Thymeleaf with Spring Boot. We are listing some of them here. 

spring.thymeleaf.mode: Template mode that will be applied on templates. Default is HTML 5 . 
spring.thymeleaf.prefix: This is the value that will be prepended with view name to build the URL. Default value is classpath:/templates/ . 
spring.thymeleaf.suffix: This is the value that will be appended with view name to build the URL. Default value is .html . 

With the default Spring Boot and Thymeleaf configuration we can keep our Thymeleaf files with html extension at following location.
src\main\resources\templates 
```
