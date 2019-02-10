## 컨트롤러 ##
```
package io.startup.elasticsearch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping(value="/")
    public String helloWorld() {
        return "Hello world";
    }
}
```


## MockMvc 를 이용한 웹 테스트 ##

Mvc 테스트는 두가지 방식을 제공해 준다. 

1. @SpringBootTest 와 @AutoConfigureMockMvc 를 사용하는 방식 (ApplicationContext 를 시작한다) 과

2. 웹 레이어만 테스트 하는 @WebMvcTest 또는 @WebMvcTest(WebController.class) 이다. 

```
package io.startup.elasticsearch;

import io.startup.elasticsearch.controller.WebController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
// @SpringBootTest
// @AutoConfigureMockMvc
@WebMvcTest(WebController.class)
public class SpringMvcTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void exampleTest() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string("Hello world"));
    }
}

```


## MockBean 을 이용한 Remote 테스트 ##

웹 레이어를 테스트 하다보면 때로는 remote 서비스를 호출하는 경우가 발생한다.

제대로 테스트 할려면 mock 서버를 두던지, 아니면 실제 remote 서비스를 실행해야 하지만, 테스트시에는 부담이 되므로, 

@MockBean 어노테이션을 사용하여 결과값을 리턴해 줄 수 있다.

```
public interface RemoteService {
    String remoteCall();
}


@Service
public class RemoteServiceImpl implements  RemoteService{

    @Override
    public String remoteCall() {
        try {
            Thread.sleep(2000);
        }
        catch(InterruptedException e) {

        }
        return "remote called";
    }
}
```

@MockBean 을 사용하기 위해서는 테스트 클래스에 @SpringBootTest 어노테이션을 선언해야 한다. 

```
package io.startup.elasticsearch;

import io.startup.elasticsearch.service.RemoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc
//@WebMvcTest(WebController.class)
public class SpringMvcTest {

    @MockBean
    private RemoteService remoteService;
  
    @Test
    public void remoteTest() throws Exception {
        given(this.remoteService.remoteCall()).willReturn("mock called");
        assertThat(remoteService.remoteCall(), is("mock called"));
    }
}
```

