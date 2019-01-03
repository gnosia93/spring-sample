
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


## JPA Entity ##
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

## JPA Repository ##
```
public interface UserJpaRepository extends MongoRepository<UserEntity, String> {

}
```

## 실행코드 ##
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




