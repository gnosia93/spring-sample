## application properties ##
```
spring.datasource.jdbc-url=jdbc:mysql://localhost:3306/sample?serverTimezone=UTC
spring.datasource.url=jdbc:mysql://localhost:3306/sample?serverTimezone=UTC
spring.datasource.username=sample
spring.datasource.password=sample
spring.datasource.maximum-pool-size=10

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Enum ##
```
public enum Gender {
	MAIL("M"),
	FEMAIL("F"),
	NOT_MAPPING("NM");
	
	String code;
	
	private Gender(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public static Gender fromCode(String code) {
		if(code == null)
			return Gender.NOT_MAPPING;
		
		switch(code) {
		case "M":
			return Gender.MAIL;
			
		case "F":
			return Gender.FEMAIL;
		}
		
		return Gender.NOT_MAPPING;
	}
}
```

## 컨버터 ##
```
@Converter(autoApply=true)
public class GenderConverter implements AttributeConverter<Gender, String> {

	@Override
	public String convertToDatabaseColumn(Gender attribute) {
		return attribute.getCode();
	}

	@Override
	public Gender convertToEntityAttribute(String dbData) {
	
		System.out.println("debug...db data from gender.." + dbData);
		return Gender.fromCode(dbData);
	}
}
```

## 엔터티 ##
```
@NoArgsConstructor
@Data
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	String name;
	
	Gender gender;
	
	public Member(String name, Gender gender) {
		this.name = name;
		this.gender = gender;
	}
}
```

## 레포지토리 인터페이스 ##
```
//@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	
}
```


## 테스트코드 ##
```
@SpringBootApplication
public class EnumconvertApplication {

	@Autowired MemberRepository memberRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(EnumconvertApplication.class, args);
	}
	
	
	@Bean
	public CommandLineRunner runner() {
		return (a) -> {
			
			System.out.println(".... command line runner...");
			/*
			Member m = new Member("name", Gender.MAIL);
			Member saved = memberRepository.save(m);
			
			Optional<Member> optMember = memberRepository.findById(saved.getId());
			System.out.println(optMember.get().getId());
			System.out.println(optMember.get().getName());
			System.out.println(optMember.get().getGender());
			*/
			
			Optional<Member> optMember = memberRepository.findById(10L);
			if(optMember.isPresent()) {
				System.out.println("id " + optMember.get().getId());
				System.out.println("name " + optMember.get().getName());
				System.out.println("gender " + optMember.get().getGender());
			}
			
			optMember = memberRepository.findById(11L);
			if(optMember.isPresent()) {
				System.out.println("id " + optMember.get().getId());
				System.out.println("name " + optMember.get().getName());
				System.out.println("gender " + optMember.get().getGender());
				if(optMember.get().getGender() == Gender.NOT_MAPPING)
					System.out.println("....true.. true..");
			}
			
		};
	}
}

```

