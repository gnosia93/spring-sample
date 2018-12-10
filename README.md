# spring-sample
this is spring boot sample with groovy spock

!! Prerequisite
1. 이클립스에서 spring boot 프로젝트를 생성한다.
2. jspresso 를 이용하여 spock nature 을 enable 시킨다.
3. 팝업 convert 메뉴에서 groovy 프로젝트로 convert 한다. 
4. spring boot 어플리케이션 시작 테스트를 한다. 
5. src/test/java 디렉토리에 샘플 spock 테스트 케이스를 아래와 같이 작성한다. 
  - maven 의존추가
  ```
                 <dependency>
		    <groupId>org.spockframework</groupId>
		    <artifactId>spock-core</artifactId>
		    <version>1.0-groovy-2.4</version>
		    <scope>test</scope>
		</dependency>
  ```		
  
  - spock testcase 생성 -> /src/test/java 디렉토리에 
  

``
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
  
    
