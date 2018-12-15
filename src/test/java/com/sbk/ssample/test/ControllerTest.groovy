package com.sbk.ssample.test;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc

import com.sbk.ssample.ui.order.controller.OrderController

import spock.lang.Specification


class ControllerTest extends Specification {
	
	//def orderController = new OrderController();
	//MockMvc mockMvc = standaloneSetup(orderController).build()
	
	def "order" () {
	
		/*
		given:
			List<Item> itemList = new ArrayList<>();
			Item item = new Item();
			item.setItemId(1);
			item.setItemName("item name-1");
			item.setItemCount(1);
			itemList.add(item);
			
			AddOrderCommand addOrderCommand = new AddOrderCommand();
			addOrderCommand.setOrderNo(0);
			addOrderCommand.setTimestamp(Instant.now())
			addOrderCommand.setItemList(itemList);
	
	
				
			// MockHttpServletRequestBuilder	
		when:
			//def response = mockMvc.perform(
			//	post('/order/add').contentType(MediaType.APPLICATION_JSON_UTF8)	)
			
		
		then:		
			//response.status == OK.value()
			 * 		
			*/
	}
	
	
	
}
