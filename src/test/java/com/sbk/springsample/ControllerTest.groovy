package com.sbk.springsample

import spock.lang.Specification

class ControllerTest extends Specification {

	def "groovy test"() {
		given:
			int left = 2
			int right = 2
 
		when:
			int result = left + right
 
		then:
			result == 4
	}
}
