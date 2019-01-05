아래 예제는 Thymeleaf 와 자바 어노테이션 및 MVC BindResult를 이용한 Form Validation 예제이다. 

폼의 명칭은 th:object="${userForm}" 로, controller 에서 userFrom 으로 바인딩 된다.

post 처리시 validation 에러가 발생하는 경우, 패스워드 매칭의 경우 global 이고, 나머지는 모두 필드에러이다. 

패스워드 매칭 어노테이션의 경우 AddUserRequest 객체에 바인딩되어 있기 때문이다. 


## registration.html (Thymeleaf template) ##

```
<html>
<body>
<h1 th:text="#{label.form.title}">form</h1>
<form action="/user/registration" th:object="${userForm}" method="POST" enctype="utf8">
    <div>
        <label th:text="#{label.user.firstName}">first</label>
        <input th:field="*{firstName}"/>
        <p th:each="error: ${#fields.errors('firstName')}"
          th:text="${error}">Validation error</p>
    </div>
    <div>
        <label th:text="#{label.user.lastName}">last</label>
        <input th:field="*{lastName}"/>
        <p th:each="error : ${#fields.errors('lastName')}"
          th:text="${error}">Validation error</p>
    </div>
    <div>
        <label th:text="#{label.user.email}">email</label>
        <input type="email" th:field="*{email}"/>
        <p th:each="error : ${#fields.errors('email')}"
          th:text="${error}">Validation error</p>
    </div>
    <div>
        <label th:text="#{label.user.password}">password</label>
        <input type="password" th:field="*{password}"/>
        <p th:each="error : ${#fields.errors('password')}"
          th:text="${error}">Validation error</p>
    </div>
    <div>
        <label th:text="#{label.user.confirmPass}">confirm</label>
        <input type="password" th:field="*{matchingPassword}"/>
        <div th:if="${#fields.hasGlobalErrors()}">
  			<p th:each="err : ${#fields.globalErrors()}" th:text="${err}">...</p>
		</div>
    </div>
    <button type="submit" th:text="#{label.form.submit}">submit</button>
</form>
 
<a th:href="@{/login.html}" th:text="#{label.form.loginLink}">login</a>
</body>
</html>
```


## MVC 컨트롤러 ##

@Valid 어노테이션으로 AddUserRequest 폼 데이터에 대해 validation 을 처리하고 있다. 

```
package com.sbk.ssample.ui.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sbk.ssample.app.domain.user.Address;
import com.sbk.ssample.app.domain.user.Gender;
import com.sbk.ssample.app.service.user.UserService;
import com.sbk.ssample.app.service.user.command.AddUserCommand;
import com.sbk.ssample.ui.user.annotation.PasswordMatches;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

	final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
		
		AddUserRequest userDto = new AddUserRequest();
		model.addAttribute("userForm", userDto);
		
		return "registration";
	}
	
	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("userForm") @Valid AddUserRequest userDto,
			BindingResult bindingResult) {
	
		if(bindingResult.hasErrors()) {
			// 에러가 발생하면 해당 페이지에 머무른다. 
			return "registration";
		};
			
		return "redirect:login";
	}
}

```


## 폼요청 객체 ##

```
@Data
@PasswordMatches                 // 패스워드 validation 
public class AddUserRequest {
    @NotNull
    @NotEmpty
    private String firstName;
     
    @NotNull
    @NotEmpty
    private String lastName;
     
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
     
    @NotNull
    @NotEmpty
    @ValidEmail                 // 이메일 validation
    private String email;
}
```

## 어노테이션 선언 ##

```
@Target({
    ElementType.TYPE, 					
    ElementType.ANNOTATION_TYPE 		
})
@Retention(RetentionPolicy.RUNTIME) 	
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
	String message() default "패스워드가 일치하지 않습니다.";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
}

@Target({
    ElementType.TYPE, 				
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE 		
})
@Retention(RetentionPolicy.RUNTIME) 	
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {

	 String message() default "이메일 주소가 올바르지 않습니다.";
	 
	 Class<?>[] groups() default {}; 
	 
	 Class<? extends Payload>[] payload() default {};
}
```

## Validation 구현 ##
```
package com.sbk.ssample.ui.user.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sbk.ssample.ui.user.annotation.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> { 

	private Pattern pattern;
    
	private Matcher matcher;
    
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"; 
    
    @Override
    public void initialize(ValidEmail constraintAnnotation) {       
    }
    
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){   
        return (validateEmail(email));
    } 
    
    private boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}


package com.sbk.ssample.ui.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sbk.ssample.ui.user.annotation.PasswordMatches;
import com.sbk.ssample.ui.user.controller.AddUserRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		
		AddUserRequest user = (AddUserRequest)value;
		return user.getPassword().equals(user.getMatchingPassword());
	}
}

```

## 레퍼런스 ##

https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html

https://www.baeldung.com/javax-validation

