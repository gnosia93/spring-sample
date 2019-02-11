## hello-service-starter ##

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
@ConditionalOnClass(HelloService.class)
public class HelloServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HelloService helloService() {
        return new HelloServiceImpl();
    }
}


# src/main/resources/META-INF/spring.factories 

org.springframework.boot.autoconfigure.EnableAutoConfiguration = \
io.startup.autoconfigure.HelloServiceConfiguration

```


## 레퍼런스 ##

https://www.javadevjournal.com/spring/spring-boot-starters/

https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-installing-spring-boot.html

https://www.javadevjournal.com/spring-boot/spring-boot-custom-starter/


