## MySQL ##
SQL> create database sample;
SQL> create user 'sample'@'%' identified by 'sample';
SQL> grant all privileges on sample.* to 'sample'@'%';
SQL> status


JPA Reference.

https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa

https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.namespace
