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
	private WebElement element;
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
		String convTitle = "Conversations";
		
		
		// verify that we are in dialogs
		try{
			
			// if we are in the right place, then nothing
			if (!driver.getTitle().equals(convTitle)){
				
				// means that we're somewhere else
				// getting back
				driver.get("https://vk.com/im");
				sleep(5000);
			}
		} catch (Exception e){
			driver.get("https://vk.com/im");
			sleep(5000);
		}
		
		try{
			element = driver.findElement(By.id("im_dialogs_search"));
			element.click();
			sleep(300);
			element.sendKeys(addressee.getName());
			element.submit();
			sleep(1000);//....
		} catch (NoSuchElementException nsee){
			
			//means that there is no this user in close
		}
	}
	
	// reads something from site
	public String read(){
		String message = null;
		driver.switchTo().window(readTab);
		
		return message;
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
}