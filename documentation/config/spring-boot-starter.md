## 스타터 패키지 작성(hello-service-starter) ##

### 1. POM ###

parent 는 spring-boot-starters 하고, spring-boot-autoconfigure 의존관계를 설정한다.

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>io.startup</groupId>
    <artifactId>hello-service-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>hello-service-spring-boot-starter</name>
    <description>Custom Starter for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starters</artifactId>
        <version>1.5.9.RELEASE</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

### 2. 소스코드 ###
```
package io.startup.autoconfigure;

public interface HelloService {
    String sayHello();
}



// @Value 오노테이션의 프로퍼티들의 경우 커스텀 starter 패키지를 사용하는 외부 프로그램에서 
// 주입받기 위해 설정한 것이다. 
package io.startup.autoconfigure;

import org.springframework.beans.factory.annotation.Value;

public class HelloServiceImpl implements  HelloService {

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.key:default-key}")
    private String applicationKey;

    public String sayHello() {

        return String.format("%s sayHello() [ %s, %s ]", HelloServiceImpl.class.getCanonicalName(), applicationName, applicationKey);

    }
}


package io.startup.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(HelloService.class)      // HelloService 클래스가 존재하는 경우에 동작
public class HelloServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean                // helloService 빈이 등록되지 않은 경우에만 동작
    public HelloService helloService() {
        return new HelloServiceImpl();
    }
}


# src/main/resources/META-INF/spring.factories 

org.springframework.boot.autoconfigure.EnableAutoConfiguration = \
io.startup.autoconfigure.HelloServiceConfiguration

```

@ConditionalOnMissingBean 어노테이션의 경우, @Compoent, @Service @Repository 등의 일반 Bean 들이 모두 스캔 및 등록된 후에

동작하므로, 순서 보장이 된다. 스프링 부트 메뉴얼에서는 autoconfigure 시에만 사용할 것을 권장하고 있다. 


### 로컬 레포지토리 등록 ###
```
$mvn clean install
```


## 스타터 테스트(starter-example) ##

### 1. POM ###
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.1.2.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>io.startup</groupId>
	<artifactId>starter-example</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>starter-example</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>io.startup</groupId>
			<artifactId>hello-service-starter</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

### 2. application properties ###
```
application.name = custom starter example
application.key = override key
# logging.level.org.springframework=DEBUG
# spring.main.allow-bean-definition-overriding=true
```

### 3. 테스트 소스 ###

HelloService 빈의 경우 커스텀 스타터에 의해 autoconfigure 되어 빈으로 등록되었으므로, 

@Autowire 해서 사용하기만 하면 된다. 

```
package io.startup.starterTest;

//import io.startup.helloServiceStarter.HelloService;
import io.startup.autoconfigure.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StarterExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarterExampleApplication.class, args);
	}

	@Autowired
	private HelloService helloService;

	@Bean
	public CommandLineRunner runner() {
		return (a) -> {
			//System.out.println(helloService);
			System.err.println(helloService.sayHello());
		};
	}
}



## 출력값.
io.startup.autoconfigure.HelloServiceImpl sayHello() [ custom starter example, override key ]
```




스타터 패키지의 @ConditionalOnMissingBean 의 동작을 확인하기 위해서 아래의 코드에 io.startup.starterTest 에 추가해 보자.

이때 주의 할 점은 인터페이스를 꼭 선언해야 한다는 점이다. 

선언하지 않는 경우 즉, HelloService 에 인터페이스가 아닌 구현 클래스 인 경우, bean duplication error 가 발생한다. (왜 그런지는 모르겠음)

스프링 부트 빈 스캔시 @Component 로 선언된 빈이 @ConditionalOnMissingBean 으로 선언된 빈보다 스캔 순위가 앞서므로,  

아래 출력값에서 볼수 있는 바와 같이 자체 HelloService 의 sayHello 가 호출되어  

io.startup.starterTest.HelloServiceImpl sayHello() 가 출력됨을 알수 있다. 



```
package io.startup.starterTest;

public interface HelloService {

    String sayHello();
}



package io.startup.starterTest;

import org.springframework.stereotype.Component;

@Component
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return HelloServiceImpl.class.getCanonicalName() + " sayHello()";
    }
}

io.startup.starterTest.HelloServiceImpl sayHello()
```






## 레퍼런스 ##

https://www.javadevjournal.com/spring/spring-boot-starters/

https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-installing-spring-boot.html

https://www.javadevjournal.com/spring-boot/spring-boot-custom-starter/


