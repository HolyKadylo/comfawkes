package com.kadylo.comfawkes;

// This is browser endpoint
public class Poster extends Node{
	
	public Poster(Account account, String sURL, int id){
		super(account, sURL, id);
		
	}
	
	// posts content to the site's wall
	// addressee is wall address (that should be checked)
	public void post (String addressee, String content){
		
	}
}