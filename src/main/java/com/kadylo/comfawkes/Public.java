package com.kadylo.comfawkes;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

public class Public{
	
	// identification number in our DB
	private int id;
	
	// link to the public
	// XXX.com/clubYYYY
	private String address;
	
	// who is our listener in this public?
	// TODO private?
	public enum ListenerRole{
		STANDALONE, ADMIN
	}
	
	// actual role
	private ListenerRole role;
  
    // Media types in public
	// TODO private?
    public enum Media{
         PICTURE, VIDEO, AUDIO, GIF
    }
  
    // media, allowed-not allowed
    private HashMap<Media, Boolean> allowedMedia;
  
    // media, where to store
    // is either account's storage or a public's one
    private HashMap<Media, String> mediaStorages;
	
	// represents banned users and the date
	// to which they are banned
	private HashMap <User, Date> bannedUsers;
	
	private int balance;
	
	// if yes, public can obtain the link to subscriber
	// by his nick
	// if yes, public can have a review of comments
	// by user's nick
	private boolean hasAccessToUsersIdentities;
	
	private User chiefAdmin;
	private ArrayList<User> secondaryAdmins;
	
	// constructing public
	public Public (String address, 
		User chief, 
		ArrayList<User> secondaryAdmins, 
		int balance, 
		int id, 
		ListenerRole role, 
		HashMap<Media, Boolean> allowed, 
		HashMap<Media, String> storage){
		
		System.out.println("-->Starting constructing public " + id);
		this.address = address;
		this.chiefAdmin = chief;
		this.secondaryAdmins = secondaryAdmins;
		this.balance = balance;
		this.id = id;
		this.role = role;
		this.allowedMedia = allowed;
		this.mediaStorages = storage;
		System.out.println("-->Public " + id + " constructed");
	}
}