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


## 테스트 클래스 ##

Mvc 테스트는 두가지 방식을 제공해 준다. 

1. @SpringBootTest 와 @AutoConfigureMockMvc 를 사용하는 방식과

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
