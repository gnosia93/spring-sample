package com.sbk.ssample.samples.cacheable;

import java.io.Serializable;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Service
public class CacheableService implements Serializable {

	@Data
	@AllArgsConstructor
	public class Book implements Serializable {
		int id;
	}
	
	void slowServiceCode() {
		try {
			Thread.sleep(3000);
		}
		catch(InterruptedException e)
		{}
	}
	
	
	@Cacheable("books")
	public Book getBook(int id) {
		slowServiceCode();
		
		return new Book(id);
	}
	
	
}
