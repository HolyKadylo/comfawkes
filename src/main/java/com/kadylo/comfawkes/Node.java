package com.kadylo.comfawkes;

// this class is used for basic selenium operations.
// Is parent to Poster and Listener
public class Node{
	
	// This is the selenium ip address, the node should 
	// talk to. Should be like http://xxx.xxx.xxx.xxx:yyyy
	private String sURL;
	
	// This is URL the node talks to
	// in constructor the part /wd/hub is added
	// to sURL
	private URL url;
	
	// unique identifier
	private int id;
	
	// logins to resource
	public void login (String email, String password){
		
	}
	
	// reads something from site
	public String read(){
		
	}
	
	
}