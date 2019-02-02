## YML 샘플 ##

스프링에서 설정파일은 /src/main/resources 디렉토리에 application.yml 로 존재한다. 

```
# 포트 및 컨텍스트 패스 설정
server:
  port: 8088
  servlet:
    context-path: /api

# H2 데이터 베이스 콘솔 설정
spring:
  h2:
    enabled: true
    path: /h2
  datasource:
    url: jdbc:h2:file:~/demo
    username: sa
    password:
    driver-class-name: org.h2.Driver
```
