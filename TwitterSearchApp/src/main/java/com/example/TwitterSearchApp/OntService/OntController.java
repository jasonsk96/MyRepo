package com.example.TwitterSearchApp.OntService;



import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OntController {
	
	@Resource(name = "resultService")
	private ResultService resultService;
	
	@PostMapping(path="/search", produces = "application/json;charset=UTF-8")
	public ResponseEntity<?> search(@RequestBody InputClass input) throws Exception{
		
		return resultService.search(input);
	}

}
