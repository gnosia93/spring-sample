package com.sbk.ssample.samples.cacheable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sbk.ssample.ui.helper.CommandResult;
import com.sbk.ssample.ui.order.controller.Controller;

@RestController
public class CacheableController implements Controller {
	Logger logger = LoggerFactory.getLogger(CacheableController.class);
	
	@Autowired CacheableService cacheableService;
	
	@RequestMapping(path="${cacheable.url}/{id}", method=RequestMethod.GET)
	@Procedure(value="appplication/json")
	public ResponseEntity<CommandResult> getCacheResult(@PathVariable int id) {
		
		CommandResult result = CommandResult.success(cacheableService.getBook(id));
		return success(result);
	}
	
	
	
}
