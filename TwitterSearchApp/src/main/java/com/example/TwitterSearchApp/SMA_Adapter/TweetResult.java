package com.example.TwitterSearchApp.SMA_Adapter;

public class TweetResult {
	String sourceImg;
	String poster;
	String postDate;
	String postText;
	String postPhoto;
	String keyword;
	
	TweetResult(String sourceImg, String poster, String postDate, String postText, String postPhoto, String keyword){
		this.sourceImg = sourceImg;
		this.poster = poster;
		this.postDate = postDate;
		this.postText = postText;
		this.postPhoto = postPhoto;
		this.keyword = keyword;
	}
	
	public String get_sourceImg() {
		return sourceImg;
	}
	
	public String get_poster() {
		return poster;
	}
	
	public String get_postDate() {
		return postDate;
	}
	
	public String get_postText() {
		return postText;
	}
	
	public String get_postPhoto() {
		return postPhoto;
	}
	
	public String get_keyword() {
		return keyword;
	}
}
