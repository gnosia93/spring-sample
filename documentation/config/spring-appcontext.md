## applicationContext.xml ##

resource 디렉토리밑에 만든다.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="
            http://www.springframework.org/schema/beans
	   		http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
	   		http://www.springframework.org/schema/context
	   		http://www.springframework.org/schema/context/spring-context-3.1.xsd">

    <bean id="manager"  class="io.startup.EmployeeFactoryBean">
        <property name="designation" value="Manager" />
    </bean>
</beans>

```



## 레퍼런스 ##
https://gist.github.com/bigme666/5210042
