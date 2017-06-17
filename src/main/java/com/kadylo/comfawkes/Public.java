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
	private enum listenerRole{
		STANDALONE, ADMIN
	}
	
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
}