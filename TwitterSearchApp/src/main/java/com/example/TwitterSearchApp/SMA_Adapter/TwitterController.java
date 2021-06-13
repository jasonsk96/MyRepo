package com.example.TwitterSearchApp.SMA_Adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import twitter4j.MediaEntity;
import twitter4j.Status;

import org.json.JSONObject;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TwitterController {
	
	@Resource(name = "twitterService")
	private TwitterService twitterService;
	  
	  @PostMapping(path="/retrieve", produces = "application/json;charset=UTF-8")
	  ResponseEntity<?> retrieve(@RequestBody Search req){
		  return twitterService.retrieve(req);
	  }
	  
	  @PostMapping(path = "/configure", consumes = "application/json;charset=UTF-8")
	  ResponseEntity<?> configure(@RequestBody Configurations config) {
		  return twitterService.configure(config);
	  }
	  
	  @GetMapping("/begin")
	  ResponseEntity<?> begin() {
		  return twitterService.begin();
	  }
}
