##  POM ##

```
<dependency>
	<groupId>org.springframework.boot</groupId>
 	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency> 
```    
```
Using Thymeleaf
If Spring Boot scans Thymeleaf library in its classpath then we are ready to work with Thymeleaf. Spring Boot provides properties for Thymeleaf that will be configured in application.properties or application.yml to change the configurations of Thymeleaf with Spring Boot. We are listing some of them here. 

spring.thymeleaf.mode: Template mode that will be applied on templates. Default is HTML 5 . 
spring.thymeleaf.prefix: This is the value that will be prepended with view name to build the URL. Default value is classpath:/templates/ . 
spring.thymeleaf.suffix: This is the value that will be appended with view name to build the URL. Default value is .html . 

With the default Spring Boot and Thymeleaf configuration we can keep our Thymeleaf files with html extension at following location.
src\main\resources\templates 
```


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
