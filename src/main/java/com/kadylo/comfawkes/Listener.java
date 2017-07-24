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
		
		switch(pub.getRole()){
			case STANDALONE:
				break;
			case ADMIN:
				
				
				// we need to be right in the user's cell
				// are we in it?
				if ( !driver.getCurrentUrl().equals("https://vk.com/gim" + pub.getId() + "?sel=" + addressee.getId()) ){
					
					//are we at least in dialogs?
					if ( !driver.getCurrentUrl().equals("https://vk.com/gim" + pub.getId())){
						driver.get("https://vk.com/gim" + pub.getId());
						sleep(5000);
						takeScreenshot("in_gim");
					}
					
					// we aren't in user's cell, but in dialogs. getting user
					element = driver.findElement(By.xpath("//*[@id='im_dialogs_search']"));
					element.sendKeys(addressee.getName());
					takeScreenshot("with_name");
					sleep(100);
					element = driver.findElement(By.xpath("//li[@data-list-id='" + addressee.getId() + "']"));
					element.click();
					sleep(150);
					takeScreenshot("inside_dialog");
				} else {
					
					//do nothing, we are in the right place
				}
				//*[@id="im_editable0"]
				element = driver.findElement(By.xpath("//*[@id='im_editable0'][2]"));
				element.click();
				element.sendKeys(content);
				takeScreenshot("with_content");
				sleep(250);
				element = driver.findElement(By.xpath("//*[@id='content']/div/div[1]/div[2]/div[3]/div[3]/div[3]/div[2]/div[1]/button"));
				element.click();
				takeScreenshot("sent");
				break;
			default:
				break;
		}
		
		//??
		/* try{
			element = driver.findElement(By.id("im_dialogs_search"));
			element.click();
			sleep(300);
			element.sendKeys(addressee.getName());
			element.submit();
			sleep(1000);//....
		} catch (NoSuchElementException nsee){
			
			//means that there is no this user in close
		} */
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