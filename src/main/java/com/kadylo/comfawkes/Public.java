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
		PICTURE, VIDEO, AUDIO // CANNOT ADD GIF
    }
  
    // media, where to store
    // is either account's storage or a public's one
	// if null then not allowed
	// no, everything is allowed
    private HashMap<Media, String> mediaStorages;
	
	public void setMediaStorage(Media media, String address){
		mediaStorages.put(media, address);
	}
	public String getMediaStorage(Media media){
		return mediaStorages.get(media);
	}
	
	// represents banned users and the date
	// to which they are banned
	private HashMap <User, Date> bannedUsers;
	private int balance;
	
	private User chiefAdmin;
	private ArrayList<User> secondaryAdmins;
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
  
	public String getAddress(){
		return address;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public void setChief(User chief){
		chiefAdmin = chief;
	}
	public User getChief(){
		return chiefAdmin;
	}
  public ListenerRole getRole(){
    return role;
  }
  public HashMap<Media, String> getMediaStorages(){
    return mediaStorages;
  }
  public int getBalance(){
    return balance;
  }
  public HashMap<User, Date> getBannedUsers(){
    return bannedUsers;
  }
  public ArrayList<User> getSecondaryAdmins(){
    return secondaryAdmins;
  }

	public void addSecondaryAdmin(User sec){
		secondaryAdmins.add(sec);
	}
	public void removeSecondaryAdmin(User sec){
		secondaryAdmins.remove(sec);
	}
	public void changeBalanceBy(int amount){
		balance += amount;
	}

	// date should be processed in Logic node
	public void banUser(User user, Date date){
		if(user.equals(chiefAdmin))
			System.out.println("-->Trying to ban chief admin");
		
		//TODO USER EQUALS & HASHCODE
		for(User usr : secondaryAdmins){
			if(usr.equals(user)){

				//making him not admin
				removeSecondaryAdmin(usr);
				bannedUsers.put(usr, date);
			}
		}
	}

	// invokes periodically
	public void unbanPeriod(){
		Date now = new Date();
		for(User u : bannedUsers.keySet()){

			// todo proper method instead of earlier
			if(bannedUsers.get(u).after(now))
				bannedUsers.remove(u);
		}
	}
      
	// invokes by demand
	public void unbanUser(User u){
		bannedUsers.remove(u);
	}

  @Override
  public boolean equals(Object obj){
    if (obj == this)
      return true;
    if (obj instanceof Public){
      Public other = (Public) obj;
      EqualsBuilder builder = new EqualsBuilder ()
        .append(getSecondaryAdmins(), other.getSecondaryAdmins())
        .append(getBannedUsers(), other.getBannedUsers())
        .append(getBalance(), other.getBalance())
        .append(getMediaStorages(), other.getMediaStorages())
        .append(getRole(), other.getRole())
        .append(getChief(), other.getChief())
        .append(getAddress(), other.getAddress())
        .append(getId(), other.getId());
      return builder.isEquals();
    }
    return false;
  }
      
  
	// constructing public
	public Public (String address, 
		User chief, 
		ArrayList<User> secondaryAdmins, 
		int balance, 
		int id, 
		ListenerRole role, 
		HashMap<Media, String> storage){
		
		System.out.println("-->Starting constructing public " + id);
		this.address = address;
		this.chiefAdmin = chief;
		this.secondaryAdmins = secondaryAdmins;
		this.balance = balance;
		this.id = id;
		this.role = role;
		this.mediaStorages = storage;
        bannedUsers = new HashMap<User, Date>();
		System.out.println("-->Public " + id + " constructed");
	}
}