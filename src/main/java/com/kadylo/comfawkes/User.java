package com.kadylo.comfawkes;

import java.util.*;

// represents user of our service
public class User{
	
	// link to user's page
	// XXX.com/idXXXXXXXX
	private String URL;
	
	// identification number in our DB
	private int id;
	
	// amount of credits, that user has
	private int balance;
	
	// nickname, which depends on resource address
	// that user wants to use
	// <resource, name>
	private HashMap <Public, String> nicknames;
	
	public User (String URL, 
		int id, 
		HashMap<Public, String> nicknames, 
		int balance){
		
		System.out.println("-->Starting constructing of user");
		this.balance = balance;
		this.URL = URL;
		this.id = id;
		this.nicknames = nicknames;
		System.out.println("-->User " + id + " constructed");
	}
	
}