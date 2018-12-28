```
    <!-- cassandra -->
    <dependency>
        <groupId>org.springframework.data</groupId>
    	<artifactId>spring-data-cassandra</artifactId>
    </dependency>
    
```


아래의 카산드라 자바 Configuration 파일이다. 이렇게 자바 Config로 등록하는 경우

application.properties 에 선언된 카산드라 관련 속성값들은 무시되는 듯 하다. 

스프링 Auto Configuration 이 어떻게 동작하는지 공부가 필요한 듯 ㅜㅜ

com.codahale.metrics.JmxReporter ClassNotFoundException 이 발생하는 경우 

CassandraCqlClusterFactoryBean 의 setJmxReportingEnabled(false) 함수를 호출하면, 더 이상 JmxReport 클래스를

찾지 않는다. 

```
@Configuration
@ConfigurationProperties(prefix="spring.data.cassandra")
public class Cassandra {
	
	 @Value("contact-points") String contactPoints;
	
	  /*
	   * Factory bean that creates the com.datastax.driver.core.Session instance
	   */ 
	  @Bean
	  public CassandraCqlClusterFactoryBean cluster() {

	    CassandraCqlClusterFactoryBean cluster = new CassandraCqlClusterFactoryBean();
	    cluster.setContactPoints("192.168.29.191");
	    //cluster.setJmxReportingEnabled(false);                      <-- com.codahale.metrics.JmxReporter ClassNotFoundException 
	    System.err.println("cassandra:" + contactPoints);
	    
	    return cluster;
	  }

	   /*
	    * Factory bean that creates the com.datastax.driver.core.Session instance
	    */
	   @Bean
	   public CassandraCqlSessionFactoryBean session() {

	    CassandraCqlSessionFactoryBean session = new CassandraCqlSessionFactoryBean();
	    session.setCluster(cluster().getObject());
	    
	    session.setKeyspaceName("mykeyspace");

	    return session;
	  }
	
}



```


아래는 JMX 관련 StackTrace 내용으로 JMXReporter 의존 관계 예외가 발생하였음을 보여주고 있다.  

```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'session' defined in class path resource [com/sbk/ssample/Cassandra.class]: Invocation of init method failed; nested exception is java.lang.NoClassDefFoundError: com/codahale/metrics/JmxReporter
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1745) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:576) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:498) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:320) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:318) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:827) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:863) ~[spring-context-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:546) ~[spring-context-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:142) ~[spring-boot-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:775) [spring-boot-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:397) [spring-boot-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:316) [spring-boot-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1260) [spring-boot-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1248) [spring-boot-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at com.sbk.ssample.StartupApplication.main(StartupApplication.java:14) [classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_121]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_121]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_121]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_121]
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:49) [spring-boot-devtools-2.1.1.RELEASE.jar:2.1.1.RELEASE]
Caused by: java.lang.NoClassDefFoundError: com/codahale/metrics/JmxReporter
	at com.datastax.driver.core.Metrics.<init>(Metrics.java:146) ~[cassandra-driver-core-3.6.0.jar:na]
	at com.datastax.driver.core.Cluster$Manager.init(Cluster.java:1501) ~[cassandra-driver-core-3.6.0.jar:na]
	at com.datastax.driver.core.Cluster.init(Cluster.java:208) ~[cassandra-driver-core-3.6.0.jar:na]
	at com.datastax.driver.core.Cluster.connectAsync(Cluster.java:376) ~[cassandra-driver-core-3.6.0.jar:na]
	at com.datastax.driver.core.Cluster.connect(Cluster.java:332) ~[cassandra-driver-core-3.6.0.jar:na]
	at org.springframework.data.cassandra.config.CassandraCqlSessionFactoryBean.connect(CassandraCqlSessionFactoryBean.java:89) ~[spring-data-cassandra-2.1.3.RELEASE.jar:2.1.3.RELEASE]
	at org.springframework.data.cassandra.config.CassandraCqlSessionFactoryBean.afterPropertiesSet(CassandraCqlSessionFactoryBean.java:82) ~[spring-data-cassandra-2.1.3.RELEASE.jar:2.1.3.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1804) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1741) ~[spring-beans-5.1.3.RELEASE.jar:5.1.3.RELEASE]
	... 21 common frames omitted
Caused by: java.lang.ClassNotFoundException: com.codahale.metrics.JmxReporter
	at java.net.URLClassLoader.findClass(URLClassLoader.java:381) ~[na:1.8.0_121]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:424) ~[na:1.8.0_121]
	at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:331) ~[na:1.8.0_121]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:357) ~[na:1.8.0_121]
	... 30 common frames omitted


```
