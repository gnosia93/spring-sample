package com.sbk.ssample.infra.gateway;

import org.springframework.web.client.RestTemplate;

public interface GateWay {
	
	public RestTemplate getRestTemplate();
	
}
