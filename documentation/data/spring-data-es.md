## POM ##

스프링 데이타 의존관계를 설정한다. 
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

## 소스코드 ##
```
# 엔티티
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = "startup", type="books")
public class Book {

    @Id
    private String id;
    private String title;
    private String author;
    private String releaseDate;
}

# 서비스 인터페이스
public interface BookService {

    Book save(Book book);
    void delete(Book book);
    Optional<Book> findById(String id);
    Iterable<Book> findAll();
    Page<Book> findByAuthor(String author, PageRequest pageRequest);
    List<Book> findByTitle(String title);
}


# 서비스 Impl
@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        this.bookRepository.delete(book);
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> findByAuthor(String author, PageRequest pageRequest) {
        return bookRepository.findByAuthor(author, pageRequest);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
}


# 레포지토리
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface BookRepository extends ElasticsearchRepository<Book, String> {

    Page<Book> findByAuthor(String author, Pageable pageable);
    List<Book> findByTitle(String title);
}

```

## 에러메시지 ##

application.properties 에 아무런 설정없이 실행하는 경우 아래와 같은 에러가 발생하는데, 이를 해결하기 위해서는

spring.data.elasticsearch.cluster-nodes=localhost:9200 를 설정해야 한다. 

아래는 스프링 부트 레퍼런스 문서의 내용을 발췌한 것으로 spring data 를 이용하여 elasticsearch 에 접속하기 위해서는

프로퍼티 설정이 필수임을 말해주고 잇다. 

31.6.3 Connecting to Elasticsearch by Using Spring Data
To connect to Elasticsearch, you must provide the address of one or more cluster nodes. The address can be specified by setting the spring.data.elasticsearch.cluster-nodes property to a comma-separated host:port list. With this configuration in place, an ElasticsearchTemplate or TransportClient can be injected like any other Spring bean, as shown in the following example:

spring.data.elasticsearch.cluster-nodes=localhost:9200

```
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in io.startup.elasticsearch.BookServiceImpl required a bean named 'elasticsearchTemplate' that could not be found.


Action:

Consider defining a bean named 'elasticsearchTemplate' in your configuration.


Process finished with exit code 0

```

## 프로퍼티 설정 ##
현재 버전의 elastic 서치는 embeded 를 지원하지 않으므로 실제 서버를 대상으로 테스트를 수행해야 한다. 

9200 포트는 rest 용이고, 9300 포트는 transport 용 포트로 자바에서 접근하는 경우 9300 포트를 사용해야 한다. 

```
spring.data.elasticsearch.cluster-nodes=192.168.29.106:9300
```

프로퍼티를 설정하는 경우 스프링 부트 시작시 아래와 같이 elasticsearch 서비스가 정상적으로 로딩되는 것을 확인할 수 있다. 
```
o.elasticsearch.plugins.PluginsService   : loaded plugin [org.elasticsearch.index.reindex.ReindexPlugin]
o.elasticsearch.plugins.PluginsService   : loaded plugin [org.elasticsearch.join.ParentJoinPlugin]
o.elasticsearch.plugins.PluginsService   : loaded plugin [org.elasticsearch.percolator.PercolatorPlugin]
o.elasticsearch.plugins.PluginsService   : loaded plugin [org.elasticsearch.script.mustache.MustachePlugin]
o.elasticsearch.plugins.PluginsService   : loaded plugin [org.elasticsearch.transport.Netty4Plugin]
o.s.d.e.c.TransportClientFactoryBean     : Adding transport node : 192.168.229.106:9300
```

## 테스트 ##

### 1. 스프링 러너 테스트 ###
```
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void emptyData() {
        bookRepository.deleteAll();
    }

    @Test
    public void shouldIndexHasJustOneBook() {
        Book book = new Book();
        book.setId("12345");
        book.setTitle("Spring Data Elasticsearch");
        book.setAuthor("automake");
        bookRepository.save(book);

        Book indexedBook = bookRepository.findById(book.getId()).get();
        assertThat(indexedBook,is(notNullValue()));
        assertThat(indexedBook.getId(), is(book.getId()));
    }
```


### 2. @ContextConfiguration 테스트 ###

/test/resources/springContext-book-test.xml 파일을 아래와 같이 만든다. 

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:elasticsearch="http://www.springframework.org/schema/data/elasticsearch"
       xsi:schemaLocation="http://www.springframework.org/schema/data/elasticsearch http://www.springframework.org/schema/data/elasticsearch/spring-elasticsearch-1.0.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd">

    <!-- elasticsearch:node-client id="client" local="true"/-->
    <elasticsearch:transport-client id="client" cluster-name="elasticsearch" cluster-nodes="startup:9300"/>

    <bean name="elasticsearchTemplate" class="org.springframework.data.elasticsearch.core.ElasticsearchTemplate">
        <constructor-arg name="client" ref="client"/>
    </bean>

    <elasticsearch:repositories base-package="io.startup.elasticsearch"/>
</beans>
```

아래는 테스트 케이스이다. 

```
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

// @RunWith(SpringRunner.class)
// @SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-book-test.xml")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void emptyData() {
        bookRepository.deleteAll();
    }

    @Test
    public void shouldIndexHasJustOneBook() {
        Book book = new Book();
        book.setId("12345");
        book.setTitle("Spring Data Elasticsearch");
        book.setAuthor("automake");
        bookRepository.save(book);

        Book indexedBook = bookRepository.findById(book.getId()).get();
        assertThat(indexedBook,is(notNullValue()));
        assertThat(indexedBook.getId(), is(book.getId()));

    }
}
```



## 레퍼런스 ##

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/     -->   31.6 Elasticsearch

https://github.com/spring-projects/spring-data-elasticsearch
