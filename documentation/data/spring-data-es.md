## POM ##
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

# 레포지토리
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface BookRepository extends ElasticsearchRepository<Book, String> {

    Page<Book> findByAuthor(String author, Pageable pageable);
    List<Book> findByTitle(String title);
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
```


## 에러메시지 ##
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



## 레퍼런스 ##

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/     -->   31.6 Elasticsearch


https://github.com/spring-projects/spring-data-elasticsearch
