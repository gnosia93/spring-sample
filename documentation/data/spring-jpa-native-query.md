## 오류 케이스 ##

1. POM 에서 data-jpa 만 추가한 경우 아래와 같은 예외 발생

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).
```

2. POM 에 H2 의존성 추가

스프링 부트의 기본 DBMS 는 H2 로 POM 에 의존성을 추가해 주면, 별다른 설정없이 사용할 수 있다. 

@Entity 테이블을 자동으로 생성해 준다. 

```
# POM
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>   


# application.properties
spring.jpa.show-sql=true


# 코드샘플
@SpringBootApplication
public class SpringJpgNativeQuery1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpgNativeQuery1Application.class, args);
	}
	
	@NoArgsConstructor
	@Data
	@Entity
	@Table(name="tb_user")
	class User {
		@Id
		long id;
		String name;
		int status;
	}

}

#log
Hibernate: drop table tb_user if exists
Hibernate: create table tb_user (id bigint not null, name varchar(255), status integer not null, primary key (id))

```



## 네이티브 쿼리 / 페이징  ##

```
#controller

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value="/jpa/select")
	public void select() {
	
		// status 값이 1인 유저 리스트
		List<User> userList = userRepository.findAllByStatus(1);
		for(User user: userList) {
			System.out.println("User " + user.getId() + " - " + user.getName() + user.getStatus());
		}
		
		// 페이지번호는 0 에서 시작한다. 3 은 페이지 사이즈이다 
		PageRequest pageRequest = PageRequest.of(0,  3);
		Page<User> pageableUserList = userRepository.findAllWithPagination(pageRequest);
		
		System.out.println("total pages : " + pageableUserList.getTotalPages());
		System.out.println("total counts : " + pageableUserList.getTotalElements());
		System.out.println("page size: " + pageableUserList.getSize());
		for(User user : pageableUserList.getContent())
			System.out.println("User " + user.getId() + " - " + user.getName() + " - " + user.getStatus());
	}


# repository 

public interface UserRepository extends JpaRepository<User, Long>{

	@Query(value="select * from tb_user where status = :status", nativeQuery = true)
	public List<User> findAllByStatus(@Param("status") int status);
	
	// 페이징 
	@Query(value="select * from tb_user order by id desc",
		   countQuery="select count(1) from tb_user", nativeQuery=true)
	public Page<User> findAllWithPagination(Pageable pageable);
}

```

## Using Container Entity Manager ##
```
@RestController
public class JpaController {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value="/jpa")
	public void insert() {
		System.out.println("....jpa insert..");
		
		nativeInsert();
	
		Query query = entityManager.createNativeQuery("select id, name, status from tb_user", User.class);
		List<User> userListByEm = query.getResultList();
		for(User user: userListByEm) 
			System.out.println("User " + user.getId() + " - " + user.getName());
	}
	
	@Transactional
	public void nativeInsert() {
		
		for(int i = 0; i < 10; i++) {
			User user = new User(i, "name" + i, 1);
			userRepository.save(user);
		}
	}
```

레포지토리를 이용하여 save 하므로 에러가 발생하지 않는다. 


## Using Application Entity Manager ##

오토와이어된 EntityManager는 스프링 컨테이너에서 생성한 엔터티 매니저로서 읽기만 지원한다. 

쓰기기는 지원하지 않음 ( 컨테이너가 생성했고, 선언적 트랜잭션만 스프링에서 지원하니깐 막아 놓은 듯 하다) 

```
@RestController
public class JpaController {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value="/jpa")
	public void insert() {
		System.out.println("....jpa insert..");
		
		nativeInsert();
	
	}
	
	@Transactional
	public void nativeInsert() {
		for(int i = 0; i < 10; i++) {
			User user = new User(i, "name" + i, 1);
			entityManager.persist(user);		
		}
	}
}

Thu Jan 24 15:28:18 KST 2019
There was an unexpected error (type=Internal Server Error, status=500).
No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call
javax.persistence.TransactionRequiredException: No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call
	at org.springframework.orm.jpa.SharedEntityManagerCreator$SharedEntityManagerInvocationHandler.invoke(SharedEntityManagerCreator.java:292)
	at com.sun.proxy.$Proxy366.persist(Unknown Source)
	at io.startup.demo.controller.JpaController.nativeInsert(JpaController.java:38)
	at io.startup.demo.controller.JpaController.insert(JpaController.java:28)
	at io.startup.demo.controller.JpaController$$FastClassBySpringCGLIB$$ed60617.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:684)
	at io.startup.demo.controller.JpaController$$EnhancerBySpringCGLIB$$b49a2fdc.insert(<generated>)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Unknown Source)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:189)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:102)
	at 
```

아래와 같이 EntityManagerFactory 에서 EntityManager 를 직접 생성해서 트랜잭션을 처리해야 오류가 발생하지 않는다. 

```
@RestController
public class JpaController {
	
	// private EntityManager entityManager;
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@GetMapping(value="/jpa")
	public void insert() {
		
		nativeInsert();
		selectAll();
	}
	
	// @Transactional
	public void nativeInsert() {
		//Use below code on create/update
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		for(int i = 0; i < 10; i++) {
			User user = new User(i, "name" + i, 1);
			// userRepository.save(user);
			entityManager.persist(user);
		}
		
		tx.commit();
		
		System.out.println(entityManager);
		entityManager.close();
	}
	
	private void selectAll() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNativeQuery("select id, name, status from tb_user", User.class);
		List<User> userListByEm = query.getResultList();
		for(User user: userListByEm) 
			System.out.println("User " + user.getId() + " - " + user.getName());
		
		System.out.println(entityManager);
		entityManager.close();
	}

```



## Multiple IN predicate / Dynamic Query ##
```

```


## 레퍼런스 ##

https://thoughts-on-java.org/jpa-native-queries/

https://www.baeldung.com/spring-data-jpa-query

https://www.logicbig.com/tutorials/spring-framework/spring-data/native-query.html

https://stackoverflow.com/questions/17860696/not-allowed-to-create-transaction-on-shared-entitymanager-use-spring-transacti

