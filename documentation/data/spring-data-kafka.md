# 기본 예제 #

https://www.youtube.com/watch?v=NjHYWEV_E_o

그냥 json 으로 보내면 될 듯..
```
class Payload<T>
{
     Localtime timestamp;
     T data;
}
```

## POM ##

```
<dependencies>
	<dependency>
		<groupId>org.springframework.kafka</groupId>
		<artifactId>spring-kafka</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.kafka</groupId>
		<artifactId>spring-kafka-test</artifactId>
		<scope>test</scope>
	</dependency>
</dependencies>

```

## 샘플 코드 ##
```

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@EnableKafka
@Configuration
public class KafkaConfig {

	String bootstrapServer = "192.168.29.123:9092";
	String topic = "spring-tutorial";
	
	
	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		return new DefaultKafkaProducerFactory<>(configs);
	}
	
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
	
	
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
	//	configs.put(ConsumerConfig.GROUP_ID_CONFIG, topic);
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	
		return new DefaultKafkaConsumerFactory<>(configs);
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = 
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		
		return factory;
	}
}


```

```
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;


@SpringBootApplication
public class SpringKafkaApplication {

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaApplication.class, args);
	}

	
	@Bean
	public CommandLineRunner runner() {
		String topic = "spring-tutorial";
		return (a) -> {
			kafkaTemplate.send(topic, "test message");
		};
	}
	
	@KafkaListener(topics="spring-tutorial", id="spring-tutorial-grp")
	public void listen(String message) {
		System.out.println("received message in group - group-id: " + message);
	}
}
```


## 에러 메시지 ##

카프카 클라이언트 라이브러리는 DNS 리버스 lookup 을 실행하는 것으로 보인다.

아래와 에러 메시지와 같이 DNS 리버스 리졸빙 이슈가 발생하면 /etc/hosts 파일에 

```
192.168.29.123          startup
```

를 등록하여 문제를 해결한다. 

```
java.io.IOException: Can't resolve address: startup:9092
	at org.apache.kafka.common.network.Selector.doConnect(Selector.java:235) ~[kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.common.network.Selector.connect(Selector.java:214) ~[kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.clients.NetworkClient.initiateConnect(NetworkClient.java:864) [kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.clients.NetworkClient.access$700(NetworkClient.java:64) [kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.clients.NetworkClient$DefaultMetadataUpdater.maybeUpdate(NetworkClient.java:1035) [kafka-clients-2.0.1.jar:na]
	at org.apache.kafka.clients.NetworkClient$DefaultMetadataUpdater.maybeUpdate(NetworkClient.java:

```


# JSON / Object Serializer 구현 #

바이트 와 자바 객체간의 변환 로직이 Serializer / Deserializer 이다.

Json 시리얼 라이저의 경우 Json 스트링을 byte 로 변환하는 것이며, object 시리얼 라이저는 자바 오브젝트를 byte 로 변환하는 것이다. 

바이트로 변환하는 이유는 네트워크로 전송하기 위함이다. 

```
public interface Serializer<T>
extends java.io.Closeable
An interface for converting objects to bytes. 
A class that implements this interface is expected to have a constructor with no parameter.

public interface Deserializer<T>
extends java.io.Closeable
An interface for converting bytes to objects. 
A class that implements this interface is expected to have a constructor with no parameters.
```	
	
```
@EnableKafka
@Configuration
public class KafkaConfig {

	String bootstrapServer = "192.168.29.123:9092";
	String topic = "spring";
	
	
	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class);
		
		return new DefaultKafkaProducerFactory<>(configs);
	}
	
	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
	
	
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
	//	configs.put(ConsumerConfig.GROUP_ID_CONFIG, topic);
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configs);
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = 
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		
		return factory;
	}
}

```

ObjectMapper 의 기능을 활용하여 byte , object 간의 변환 기능을 구현한다. 

```
// Serializer (Object --> Byte 구현)

import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomSerializer<T> implements Serializer<T> {

	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String topic, T data) {
		byte[] byteData = null;
		try {
			byteData = objectMapper.writeValueAsBytes(data);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return byteData;
	}

	@Override
	public void close() {
	}
}


// Deserializer (Byte --> Object 변환 구현)

import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomDeserializer<T> implements Deserializer<T> {
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(String topic, byte[] data) {
		try {
			return (T)objectMapper.readValue(data, Object.class);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
		
	}
}
```

소스상에서 ObjectMapper 를 사용하여 자바 오브젝트를 json 으로 변환해야 하는 Json Serilizor 에 비해 간단한 

코드로 로직을 구현할 수 있게 되었다. 


```
@SpringBootApplication
public class SpringKafkaApplication {

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner() {
		String topic = "spring";

		return (a) -> {
			try {
				
				System.out.println("send..data");
				/*
				ObjectMapper objectMapper = new ObjectMapper();
				Payload payload = new Payload(1, "test");
				String data = objectMapper.writeValueAsString(payload);
				kafkaTemplate.send(topic, data);
				*/
				
				Payload payload = new Payload(1, "test");
				kafkaTemplate.send(topic, "payload-key", payload);
				
				Employee emp = new Employee(2, "firstName", "lastName");
				kafkaTemplate.send(topic, "emp-key", emp);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		};
	}
	
	@KafkaListener(topics="spring", id="spring-tutorial-grp")
	public void listen(Object obj) {
		System.out.println("received message in group " + obj);
	}
}
```





http://wpcertification.blogspot.com/2016/12/sending-and-receiving-json-messages-in.html

https://stackoverflow.com/questions/41141924/send-custom-java-objects-to-kafka-topic


## Object Serialization ##

각 데이타 타입에 대해 Serializable 인터페이스를 구현한다. 

```
@Getter
public class Employee implements Serializable {
	int id;
	String firstName;
	String lastName;
		
	public Employee(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
		
}

@Getter
public class Payload implements Serializable {
	long id;
	String title;
	LocalTime time;
	
	public Payload(long id, String title) {
		this.id = id;
		this.title = title;
		this.time = LocalTime.now();
	}
}


```

오브젝트 시리얼라이즈를 사용하는 경우 카프카 메시지는 오브젝트 Byte 값이 저장되므로, 알아 볼수 없는 값이다.

이와는 반대로 Json 시리얼 라지르를 사용하는 경우는 json string 포맷으로 저장된다. 

```
	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(String topic, byte[] data) {
		Object obj = null;
		try {
		//	return (T)objectMapper.readValue(data, Object.class);
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			
			ois.close();
			bis.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return (T)obj;
	}
	
	@Override
	public byte[] serialize(String topic, T data) {
		byte[] byteData = null;
		try {
			//byteData = objectMapper.writeValueAsBytes(data);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			oos.close();
			
			byteData = baos.toByteArray();
			baos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return byteData;
	}
```

```
@SpringBootApplication
public class SpringKafkaApplication {

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaApplication.class, args);
	}

	
	@Bean
	public CommandLineRunner runner() {
		String topic = "spring";
	
		
		return (a) -> {
			try {
				Payload payload = new Payload(1, "test");
				kafkaTemplate.send(topic, "payload-key", payload);
				
				Employee emp = new Employee(2, "firstName", "lastName");
				kafkaTemplate.send(topic, "emp-key", emp);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		};
	}
	
	@KafkaListener(topics="spring", id="spring-tutorial-grp")
	public void listen(ConsumerRecord<String, Object> record) {
		System.out.println("received message in group " + record);
		if(record.value() instanceof Employee) {
			Employee e = (Employee)record.value();
			System.out.println(e.getFirstName());
		}
		
	}
}
```


## Spring Data Kafka ##

https://docs.spring.io/spring-kafka/reference/htmlsingle/

## Spring Boot Kafka Code Sample ##

https://www.baeldung.com/spring-kafka

http://cloudurable.com/blog/kafka-tutorial-kafka-producer/index.html


## Kafka ##

* 아키텍처 http://epicdevs.com/17

* 클러스터 구축 http://epicdevs.com/20

* 자바 코드 샘플 http://epicdevs.com/21?category=460351

* Apache Kafka Tutorial https://www.youtube.com/watch?v=gg-VwXSRnmg&list=PLkz1SCf5iB4enAR00Z46JwY9GGkaS2NON
