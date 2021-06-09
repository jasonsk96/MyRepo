package com.example.TwitterSearchApp.SMA_Adapter;

public class Configurations {
	String AuthConsumerKey;
	String AuthConsumerSecret;
	String AuthAccessToken;
	String AccessTokenSecret;
	String TimeDelay;
	String Max_Tweets;
	String KeyWords;
	
	Configurations(String AuthConsumerKey, String AuthConsumerSecret, String AuthAccessToken, String AccessTokenSecret, String TimeDelay, String Max_Tweets, String KeyWords){
		this.AuthConsumerKey = AuthConsumerKey;
		this.AuthConsumerSecret = AuthConsumerSecret;
		this.AuthAccessToken = AuthAccessToken;
		this.AccessTokenSecret = AccessTokenSecret;
		this.TimeDelay = TimeDelay;
		this.Max_Tweets = Max_Tweets;
		this.KeyWords = KeyWords;
	}
}