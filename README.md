### spring-sample
스프링 부트 단위 테스트를 spock 을 이용한 예제이다. 

### Prerequisite
1. 이클립스에서 spring boot 프로젝트를 생성한다.
1. jspresso 를 이용하여 spock nature 을 enable 시킨다.
1. 팝업 convert 메뉴에서 groovy 프로젝트로 convert 한다. 
1. spring boot 어플리케이션 시작 테스트를 한다. 
1. src/test/java 디렉토리에 샘플 spock 테스트 케이스를 아래와 같이 작성하고, maven 에 spock-core 라이브러리에 대한 의존성을 추가한다. 
  
  1.maven 의존추가
  ```
	<dependency>
	   <groupId>org.spockframework</groupId>
	    <artifactId>spock-core</artifactId>
	    <version>1.0-groovy-2.4</version>
	    <scope>test</scope>
	</dependency>
  ```		
  
  2.spock testcase 생성 -> /src/test/java 디렉토리에 
 
```
  package com.sbk.springsample`
  import spock.lang.Specification

  class ControllerTest extends Specification {

    def "groovy test"() {
      given:
        int left = 2
        int right = 2

      when:
        int result = left + right

      then:
        result == 4
    }
  }
```
  

### boot testing ###
#### 테스트팅 튜토리얼 ####
https://www.baeldung.com/spring-boot-testing

#### 부트 테스트 레퍼런스 ####
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html

  
MVC 단위 테스트 예제
https://memorynotfound.com/unit-test-spring-mvc-rest-service-junit-mockito/#unit-test-http-post
    
