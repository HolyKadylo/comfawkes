package com.kadylo.comfawkes;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.CompositeAction;
import java.util.ArrayList;

// This is browser endpoint
public class Listener extends Node{
	
	// users that we ignore for a while or forever
	// this list is renewed in each message
	// from logic node.
	// if user is ignored, then messages from him
	// are not opened at all
	private ArrayList<User> ignoredUsers;
    private String readTab;
    private String writeTab;
	
	public Listener(Account account, String sURL, int id){
		super(account, sURL, id);
		System.out.println("-->Continuing creation of Listener " + id);
        String s = Keys.chord(Keys.CONTROL, "t");
        driver.get("https://google.com");
        sleep(2000);
        ((JavascriptExecutor)driver).executeScript("window.open('HTTPS://Google.com','_blank');");
        ArrayList<String> handles = new ArrayList <String> (driver.getWindowHandles());
        writeTab = handles.get(0);
        readTab = handles.get(1);
        driver.switchTo().window(readTab);
		ignoredUsers = new ArrayList<User>();
		System.out.println("-->Listener " + id + " was created");
	}
	
	// will be invoked internally
	private void setIgnoredUsers(ArrayList<User> list){
		ignoredUsers = list;
	}
	
	// will be invoked ??
	private ArrayList<User> getIgnoredUsers(){
		return ignoredUsers;
	}
	
	// posts message to site user in dialog
	public void post (User addressee, String content){
		boolean needToGetUser = false;
		switch(pub.getRole()){
			case STANDALONE:
				
				// we need to be right in the user's cell
				// are we in it?
				if ( !driver.getCurrentUrl().equals("https://vk.com/im?sel=" + addressee.getId()) ){
					
					//are we at least in dialogs?
					if ( !driver.getCurrentUrl().equals("https://vk.com/im")){
						driver.get("https://vk.com/im");
						sleep(5000);
					}
					needToGetUser = true;
				}
				break;
			case ADMIN:
				
				// we need to be right in the user's cell
				// are we in it?
				if ( !driver.getCurrentUrl().equals("https://vk.com/gim" + pub.getId() + "?sel=" + addressee.getId()) ){
					
					//are we at least in dialogs?
					if ( !driver.getCurrentUrl().equals("https://vk.com/gim" + pub.getId())){
						driver.get("https://vk.com/gim" + pub.getId());
						sleep(5000);
					}
					needToGetUser = true;
				}
				break;
			default:
				break;
		}
		
		// we aren't in user's cell, but in dialogs. getting user
		if (needToGetUser){
			element = driver.findElement(By.xpath("//*[@id='im_dialogs_search']"));
			element.sendKeys(addressee.getName());
			sleep(100);
			element = driver.findElement(By.xpath("//li[@data-list-id='" + addressee.getId() + "']"));
			element.click();
			sleep(150);
		}
		element = driver.findElement(By.xpath("//*[@id='im_editable0']"));
		element.click();
		element.sendKeys(content);
		sleep(250);
		element.sendKeys(Keys.RETURN);
	}
	
	// attempts to read the site. if success, returns message
	// otherwise null
	public Message read(){
		// user constr
		if ( driver.getCurrentUrl().equals("https://vk.com/im") ){
			
			// means we are in simple messages
			try { 
				element = driver.findElement(By.xpath("//li[@class='nim-dialog_unread']"));
				//element = driver.findElement(By.xpath("//span[@class='left_count']"));
			} catch (NoSuchElementException nsee){
				
				// means there is no count i.e no new messages
				System.out.println("-->No new messages");
				return null;
			}
			//element = driver.findElement(By.xpath("//li[@class='nim-dialog_recent']"));
			element.click();
			sleep(250);
			element = driver.findElement(By.xpath("//*[@id='content']/div/div/div[3]/div[3]/div[2]/div/div/div[1]/div[1]/div/div[1]/div/div/div[1]/div[5]/div[2]/ul/li/div[3]/div"));
			//element.getText();
			
			// TODO return user from DB
			// returning message
			try {
				System.out.println("-->Returning " + element.getText());
				String text = element.getText();
				int uid = Integer.parseInt(driver.getCurrentUrl().substring(22));
				driver.get("https://vk.com/im");
				sleep(250);
				return this.new Message(text, new User(uid));
			} catch (NumberFormatException nfe){
				System.out.println("-->Exception while parsing id: " + nfe.toString());
			}
		} else {
			if (driver.getCurrentUrl().contains("im?sel=")){
				
				// means we are in some user
			}
		}
		System.out.println("-->Not in messages, waiting, re-entering");
		sleep(25000);
		driver.get("https://vk.com/im");
		read();
		System.out.println("-->Something wrong");
		return null;
		// public User (String URL, int id, HashMap<Public, String> nicknames, String name, int balance){
		//Listener.Message message = this.new Message("abc", new User("123", 12, null, "123", 123)); 
		//driver.switchTo().window(readTab);
		
		//return message;
	}
	
	//is used in next method
	private void clickElementById(String id){
		element = driver.findElement(By.id(id));
		element.click();
		sleep(5000);	
	}

	// listens
	// messages are open
	private void setSettings(){
		System.out.println("-->Setting settings on Listener " + id);
		try{
			driver.get("https://vk.com/feed");
			String lang = "English";
			sleep(5000);
			clickElementById("top_profile_link");
			clickElementById("top_settings_link");
			clickElementById("settings_video_autoplay");
			clickElementById("settings_gif_autoplay");
			clickElementById("settings_stickers_hints");
			clickElementById("chglang");
			element = driver.findElement(By.xpath("//*[text() = '" + lang + "']"));
			element.click();
			sleep(5000); 
			driver.navigate().refresh();
			sleep(5000); 
			clickElementById("ui_rmenu_privacy");
			clickElementById("privacy_edit_photos_saved");
			clickElementById("privacy_item3"); 
			clickElementById("privacy_edit_groups"); 
			clickElementById("privacy_item3");
			clickElementById("privacy_edit_wall_send");
			clickElementById("privacy_item3"); 
			clickElementById("privacy_edit_status_replies"); 
			clickElementById("privacy_item3");
			clickElementById("privacy_edit_mail_send");
			clickElementById("privacy_item0");
			clickElementById("privacy_edit_appscall");
			clickElementById("privacy_item3"); 
			clickElementById("privacy_edit_groups_invite"); 
			clickElementById("privacy_item3");  
			clickElementById("privacy_edit_apps_invite");
			clickElementById("privacy_item3");
			
			// veryfying that we are in old messages
			String oldMessagesMark1 = "Unread";
			String oldMessagesMark2 = "Starred messages";
			String switchTo = "Switch to new interface";
			try{
			driver.get("https://vk.com/im");
			sleep(4000);
			element = driver.findElement(By.xpath("//*[text() = '" + oldMessagesMark1 + "']"));
			element = driver.findElement(By.xpath("//*[text() = '" + oldMessagesMark2 + "']"));
			} catch (NoSuchElementException nsee){
				return;
				//means we are in new messages already
			}
			element = driver.findElement(By.xpath("//*[text() = '" + switchTo + "']"));
			element.click();
			sleep(3900);
			System.out.println("-->Settings on Listener " + id +" were successfully set");
		} catch (Exception e){
			System.out.println("-->Error while setting settings");
			e.printStackTrace();
		}
	}
	
	// Message that returns read()
	class Message{
		private String content;
		private User user;
		
		private Message(String content, User user){
			System.out.println("-->Creating Message");
			this.content = content;
			this.user = user;
			System.out.println("-->Message created");
		}
		
		public String getContent(){
			return content;
		}
		
		public User getUser(){
			return user;
		}
	}
	
}