## applicationContext.xml ##

/src/main/resource 디렉토리밑에 만든다.

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

```
@Data
public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private String designation;
}

public class EmployeeFactoryBean extends AbstractFactoryBean<Employee>
{

    private String designation;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public Class<?> getObjectType() {
        return Employee.class;
    }

    @Override
    protected Employee createInstance() throws Exception {
        Employee e = new Employee();
        e.setDesignation(getDesignation());
        return e;
    }
}
```

```
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {
    public static void main(String[] args)
    {
        System.out.println("main...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Employee e = context.getBean("manager", Employee.class);
        System.out.println(e);

    }
}
```



## 레퍼런스 ##
https://gist.github.com/bigme666/5210042
