##  POM ##

```
<dependency>
	<groupId>org.springframework.boot</groupId>
 	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency> 
```    

Using Thymeleaf
If Spring Boot scans Thymeleaf library in its classpath then we are ready to work with Thymeleaf. 

Spring Boot provides properties for Thymeleaf that will be configured in application.properties or 

application.yml to change the configurations of Thymeleaf with Spring Boot. We are listing some of them here. 

spring.thymeleaf.mode: Template mode that will be applied on templates. Default is HTML 5 . 

spring.thymeleaf.prefix: This is the value that will be prepended with view name to build the URL. Default value is classpath:/templates/ . 

spring.thymeleaf.suffix: This is the value that will be appended with view name to build the URL. Default value is .html . 

With the default Spring Boot and Thymeleaf configuration we can keep our Thymeleaf files 

with html extension at following location.

src\main\resources\templates 



## registration.html ##
```
<html>
<body>
<h1 th:text="#{label.form.title}">form</h1>
<form action="/" th:object="${user}" method="POST" enctype="utf8">
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
    </div>
    <button type="submit" th:text="#{label.form.submit}">submit</button>
</form>
 
<a th:href="@{/login.html}" th:text="#{label.form.loginLink}">login</a>
</body>
</html>
```


## Controller ##
```
package com.sbk.ssample.ui.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.sbk.ssample.app.domain.user.Address;
import com.sbk.ssample.app.domain.user.Gender;
import com.sbk.ssample.app.service.user.UserService;
import com.sbk.ssample.app.service.user.command.AddUserCommand;

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
	
	@GetMapping("/add")
	public void getUser() {
		//AddUserRequest --> AddUserCommand --> User Domain Entity --> repository(I)
		
		AddUserCommand addUserCommand = new AddUserCommand();
		addUserCommand.setId("mongo-id-test-01");
		addUserCommand.setGender(Gender.FEMAIL);
		addUserCommand.setPassword("password");
		addUserCommand.setGender(Gender.MAIL);
		addUserCommand.setName("name");
		addUserCommand.setAddress(new Address("00000", "서울시", "강동구"));
		
		userService.add(addUserCommand);
		log.info("UserController is called..");
	}
	
	
	@GetMapping("/registration")
	public String showRegistrationForm(WebRequest request, Model model) {
		
		UserDto userDto = new UserDto();
		model.addAttribute("user", userDto);
		
		return "registration";
	}
	
	
	
}

```


## DTO ##
```
package com.sbk.ssample.ui.user.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserDto {
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
    private String email;
     
    // standard getters and setters
}
```
