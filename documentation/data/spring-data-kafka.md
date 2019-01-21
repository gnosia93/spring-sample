## 에러 메시지 ##

카프카 클라이언트 라이브러는 DNS 리버스 lookup 을 실행하는 것으로 보인다.

아래와 같이 DNS 리버스 리졸빙 이슈가 발생하면 /etc/hosts 파일에 

192.168.29.123          startup

를 등록하도록 한다. 

```
2019-01-21 22:29:53.163[0;39m [33m WARN[0;39m [35m4152[0;39m [2m---[0;39m [2m[orial-dev-0-C-1][0;39m [36morg.apache.kafka.clients.NetworkClient  [0;39m [2m:[0;39m [Consumer clientId=consumer-2, groupId=spring-tutorial-dev] Error connecting to node startup:9092 (id: 0 rack: null)

java.io.IOException: Can't resolve address: startup:9092
	at org.apache.kafka.common.network.Selector.doConnect(Selector.java:235) ~[kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.common.network.Selector.connect(Selector.java:214) ~[kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.clients.NetworkClient.initiateConnect(NetworkClient.java:864) [kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.clients.NetworkClient.access$700(NetworkClient.java:64) [kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.clients.NetworkClient$DefaultMetadataUpdater.maybeUpdate(NetworkClient.java:1035) [kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.clients.NetworkClient$DefaultMetadataUpdater.maybeUpdate(NetworkClient.java:



```



## Spring Data Kafka ##

https://docs.spring.io/spring-kafka/reference/htmlsingle/

## Spring Boot Kafka Code Sample ##

https://www.baeldung.com/spring-kafka


## Kafka ##

* 아키텍처 http://epicdevs.com/17

* 클러스터 구축 http://epicdevs.com/20

* 자바 코드 샘플 http://epicdevs.com/21?category=460351


