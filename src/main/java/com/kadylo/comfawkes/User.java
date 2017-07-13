package com.kadylo.comfawkes;

import java.util.HashMap;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
    public int getId(){
      return id;
    }
	public String getURL(){
		return URL;
	}
	public int getBalance(){
		return balance;
	}
	public HashMap<Public, String> getNicknames(){
		return nicknames;
	}

	@Override
	public int hashCode(){
		HashCodeBuilder builder = new HashCodeBuilder()
			.append(getId())
			.append(getName())
			.append(getBalance())
			.append(getURL())
			.append(getNicknames());
		return builder.toHashCode();
	}

	@Override
	public boolean equals (Object obj){
		/* if(obj = (Object) this)
			return true; */
		if ( obj instanceof User){
			User other = (User) obj;
			EqualsBuilder builder = new EqualsBuilder()
				.append(getId(), other.getId())
				.append(getName(), other.getName())
				.append(getURL(), other.getURL())
				.append(getBalance(), other.getBalance())
				.append(getNicknames(), other.getNicknames());
			return builder.isEquals();
		}
		return false;
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