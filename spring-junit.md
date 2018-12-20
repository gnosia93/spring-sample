#### JUnit / Hamcrest / Mockito 의존성 설정 ####
JUnit 4.11 을 사용하고 hamcrest-core 패키지는 제외시키고 hamcrest-library 의존성에 추가한다. 
hamcrest-core 패키지를 junit 에서 제외시키지 않는 경우 일부 매처 함수들이 제대로 동작하지 않는데.
이는 junit 과 hamcrest-library 에서 동일 패키지를 구현하고 있기 때문이다.
즉 core 로의 호출을 library 쪽 호출로 바꿔주기 위해 동일 시그니처를 가진 junit hanmcrest 를 의존관계에서
제외시키기 위함이다. 

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>io.startup</groupId>
  <artifactId>test</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>test</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
      
      <exclusions>                       <---- 패키지 제외
        <exclusion>
            <artifactId>hamcrest-core</artifactId>
            <groupId>org.hamcrest</groupId>
        </exclusion>
      </exclusions> 
      
    </dependency>
    	<!-- https://mvnrepository.com/artifact/org.hamcrest/hamcrest-library -->
	<dependency>
	    <groupId>org.hamcrest</groupId>
	    <artifactId>hamcrest-library</artifactId>
	    <version>1.3</version>
	    <scope>test</scope>
	</dependency>
	<!-- mockito -->
	<dependency>
		<groupId>org.mockito</groupId>
		<artifactId>mockito-core</artifactId>
		<version>2.23.4</version>
	</dependency>	

	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<version>1.18.2</version>
	</dependency>
  </dependencies>
</project>
```

#### AssertThat 및 Matchers / Mockito 사용 ####
해당 static 함수들을 사용하기 위해서는 아래와 같이 Matchers 패키지 하부에 있는 함수를 static import 해야 한다. 

```
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;            <--- BDD 형식으로 테스트 할때 // given // when // then.
````




#### CoreMatchers Java Docs ####

https://junit.org/junit4/javadoc/4.11/org/hamcrest/CoreMatchers.html



#### Hamcrest for testing Tutorial ####
http://www.vogella.com/tutorials/Hamcrest/article.html


#### Mokito Tutorial ####
http://static.javadoc.io/org.mockito/mockito-core/2.23.4/org/mockito/Mockito.html


## JUnit 5 시작하기 ##
https://junit.org/junit5/docs/current/user-guide/

pom.xml 에 아래 내용만 추가하면 된다. 
```
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.1.0</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.1.0</version>
    <scope>test</scope>
</dependency>
```

IMPORT
```
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;
```





