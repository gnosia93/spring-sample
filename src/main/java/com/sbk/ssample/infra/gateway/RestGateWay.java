package com.sbk.ssample.infra.gateway;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestGateWay implements GateWay {

	RestTemplate restTemplate;
	
	private RestGateWay() {

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); 
		factory.setReadTimeout(5000); // 읽기시간초과, ms 
		factory.setConnectTimeout(3000); // 연결시간초과, ms 
		HttpClient httpClient = HttpClientBuilder.create() 
				.setMaxConnTotal(5) // connection pool 적용 
				.setMaxConnPerRoute(5) // connection pool 적용 
				.build(); 
		factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅 RestTemplate 
		
		this.restTemplate = new RestTemplate(factory);
	}

	@Override
	public RestTemplate getRestTemplate() {
		return this.restTemplate;
	}
	
}
