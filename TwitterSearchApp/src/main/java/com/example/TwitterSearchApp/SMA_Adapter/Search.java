package com.example.TwitterSearchApp.SMA_Adapter;

import java.util.List;

public class Search {
	String[] keywords;
	//String[] languages;
	String place;
	String logic;
	
	public Search(List<String> keywords, String place, String logic){
		this.keywords = keywords.toArray(new String[keywords.size()]);
		this.place = place;
		this.logic = logic;
	}
}