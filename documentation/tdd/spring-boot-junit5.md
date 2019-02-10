## POM ##

스프링 부트에서 JUnit5 를 실행하기 위한 의존 관계 설정이다. spring-boot-starter-test 에 덧붙여서 junit-jupiter-api 와 

junit-jupiter-engine 의 의존 관계를 추가해 주면 된다. 

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>
<dependency>
	<groupId>org.junit.jupiter</groupId>
	<artifactId>junit-jupiter-api</artifactId>
</dependency>
<dependency>
	<groupId>org.junit.jupiter</groupId>
	<artifactId>junit-jupiter-engine</artifactId>
</dependency>
```



## 레퍼런스 ##

https://github.com/howtoprogram/junit5-examples/blob/master/junit5-spring-boot-example/pom.xml

https://dzone.com/articles/spring-boot-2-with-junit-5-and-mockito-2-for-unit
