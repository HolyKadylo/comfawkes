package com.kadylo.comfawkes;

import java.util.HashMap;

// represents user of our service
public class User{
	
	// link to user's page
	// XXX.com/idXXXXXXXX
	private String URL;
	
	// identification number in our DB
	private int id;
	
	// amount of credits, that user has
	private int balance;
	
	// Real name of user in English version of vk
	private String name;
	
	// nickname, which depends on resource address
	// that user wants to use
	// <resource, name>
	private HashMap <Public, String> nicknames;
	
	public String getName(){
		return name;
	}
	
	//TODO generate id?
	public User (String URL, 
		int id, 
		HashMap<Public, String> nicknames, 
		String name, 
		int balance){
		
		System.out.println("-->Constructing user");
		this.balance = balance;
		this.URL = URL;
		this.id = id;
		this.nicknames = nicknames;
		this.name = name;
		System.out.println("-->User " + id + " constructed");
	}
	
}