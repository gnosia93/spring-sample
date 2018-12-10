package com.sbk.springsample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class OrderControllerTest {

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	@Test
	public void testCase() throws Exception {
		String content = "";
		
		mockMvc.perform(post("/order/add")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content))
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	
}
