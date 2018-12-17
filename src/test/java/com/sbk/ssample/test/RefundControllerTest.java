package com.sbk.ssample.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.sbk.ssample.base.CommandResult;
import com.sbk.ssample.infra.gateway.RestGateWay;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RefundControllerTest {

	@Autowired RestGateWay gateWay; 
	
	
	@Test 
	public void refundTest() {
		
		
		for(int i = 0; i < 10; i++) {
			ResponseEntity<CommandResult> result = 
					gateWay.getRestTemplate()
						   .getForEntity("http://localhost:8080/refund/1", CommandResult.class);
			
			System.out.println(result.getStatusCodeValue());
			System.out.println(i + " " + result.getBody().toString());
			
		}
	}
	
	
}
