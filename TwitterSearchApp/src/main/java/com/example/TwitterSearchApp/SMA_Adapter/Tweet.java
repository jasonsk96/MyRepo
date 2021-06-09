package com.example.TwitterSearchApp.SMA_Adapter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import twitter4j.Status;

@Entity
public class Tweet {
	
	private @Id @GeneratedValue Long id;
	@Lob
	private Status myTweet;
	
	Tweet(){}
	
	Tweet(Status myTweet){
		this.myTweet = myTweet;
	}

	public Status getTweet() {
		return myTweet;
	}
	
	public Long getId() {
	    return this.id;
	  }
}
