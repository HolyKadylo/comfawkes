package com.kadylo.comfawkes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

// This is browser endpoint
public class Poster extends Node{
	
	private WebElement element;
	
	public Poster(Account account, String sURL, int id){
		super(account, sURL, id);
		
	}
	
	// posts content to the site's wall
	// addressee is wall address (that should be checked)
	public void post (String addressee, String content){
		
	}
	
	//is used in next method
	private void clickElementById(String id){
		element = driver.findElement(By.id(id));
		element.click();
		sleep(5000);	
	}
	
	// doesn't listen
	// all's closed
	// gifts are open
	private void setSettings(){
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
		clickElementById("privacy_item1");
		clickElementById("privacy_edit_appscall");
		clickElementById("privacy_item3"); 
		clickElementById("privacy_edit_groups_invite"); 
		clickElementById("privacy_item3");  
		clickElementById("privacy_edit_apps_invite");
		clickElementById("privacy_item3");
	}
}