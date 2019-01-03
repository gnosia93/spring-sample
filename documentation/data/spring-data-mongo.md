## POM ##
```
<dependency>
  <groupId>org.springframework.data</groupId>
	<artifactId>spring-data-mongodb</artifactId>
</dependency>
```

## application.yml ##
```
spring.data.mongodb.host= 192.168.29.191
spring.data.mongodb.database = sample
spring.data.mongodb.username = sample
spring.data.mongodb.password = sample

# MONGODB (MongoProperties)
spring.data.mongodb.authentication-database= # Authentication database name.
spring.data.mongodb.field-naming-strategy= # Fully qualified name of the FieldNamingStrategy to use.
spring.data.mongodb.grid-fs-database= # GridFS database name.
spring.data.mongodb.port= # Mongo server port. Cannot be set with URI.
spring.data.mongodb.repositories.type=auto # Type of Mongo repositories to enable.
spring.data.mongodb.uri=mongodb://localhost/test # Mongo database URI. Cannot be set with host, port and credentials.
```






## JPA Repository Example ##

### JPA Entity ###
아래와 같이 @Id 어노테이션을 사용하지 않는 경우 mongodb는 컬렉션 document 의 PK를 자체적으로 생성해 준다.

PK 인 _id 값은 아래와 같은 방식으로 구현되어 있다.

https://www.vividcortex.com/blog/what-is-mongodbs-_id-field-and-how-to-use-it

```
@Document
@NoArgsConstructor
@Data
public class UserEntity {

	@Id
	String id;
	UserType userType;
	String name;
	Gender gender;
	Address address;
}
```

### JPA Repository ###
```
public interface UserJpaRepository extends MongoRepository<UserEntity, String> {

}
```

### 실행코드 ###
```
public interface UserRepository {
	public void addUser(User user);
	public Optional<User> findById(String id);
}


@Slf4j
@Component
public class UserRepositoryImpl implements UserRepository {

	UserJpaRepository userJpaRepository;
	
	@Autowired
	public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}
	
	@Override
	public void addUser(User user) {
		
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setAddress(user.getAddress());
		userEntity.setGender(user.getGender());
		userEntity.setName(user.getName());
		userEntity.setUserType(user.getUserType());
		
		UserEntity saved = userJpaRepository.save(userEntity);
		log.info("===> " + saved.getId() + " " + saved.getName());
	}

	@Override
	public Optional<User> findById(String id) {
		return null;
	}
}
```

### Document 조회 ###
```
> show collections;
addUserCommand
sample
system.indexes
userEntity

> db.userEntity.find()
{ "_id" : ObjectId("5c2e1864210ae00b788666fd"), "name" : "name", "gender" : "MAIL", "_class" : "com.sbk.ssample.infra.order.jpa.entity.UserEntity" }
{ "_id" : ObjectId("5c2e18ad210ae00b78866700"), "name" : "name", "gender" : "MAIL", "_class" : "com.sbk.ssample.infra.order.jpa.entity.UserEntity" }
{ "_id" : "mongo-id-test-01", "name" : "name", "gender" : "MAIL", "address" : { "zipCode" : "00000", "addr1" : "서울시", "addr2" : "강동구" }, "_class" : "com.sbk.ssample.infra.order.jpa.entity.UserEntity" }
```


### 레퍼런스 ###
https://spring.io/guides/gs/accessing-data-mongodb/


## MongoDB Template ##
```


```




