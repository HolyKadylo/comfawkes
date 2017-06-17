package com.kadylo.comfawkes;

// This is browser endpoint
public class Listener extends Node{
	
	public Listener(Account account, String sURL, int id){
		super(account, sURL, id);
		
	}
	
	// posts message to site user in dialog
	public void post (User addressee, String content){
		
	}
	
	// reads something from site
	public String read(){
		String message = null;
		
		return message;
	}
}