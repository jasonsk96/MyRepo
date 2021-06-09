package com.example.TwitterSearchApp.SMA_Adapter;

public class TweetNotFoundException extends RuntimeException{

	TweetNotFoundException(Long id) {
	    super("Could not find employee " + id);
	  }
}
