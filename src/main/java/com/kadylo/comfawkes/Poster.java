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
	public void setSettings(){
		driver.get("https://vk.com/feed");
      System.out.println("-->");
		String lang = "English";
		sleep(5000);
		clickElementById("top_profile_link");
System.out.println("-->q");
		clickElementById("top_settings_link");
System.out.println("-->w");
		clickElementById("settings_video_autoplay");
System.out.println("-->e");
		clickElementById("settings_gif_autoplay");
System.out.println("-->r");
		clickElementById("settings_stickers_hints");
System.out.println("-->t");
		clickElementById("chglang");
System.out.println("-->y");
	    element = driver.findElement(By.xpath("//*[text() = '" + lang + "']"));
System.out.println("-->u");
		element.click();
System.out.println("-->i");
		sleep(5000); 
System.out.println("-->ii");
		driver.navigate().refresh();
		sleep(5000); 
System.out.println("-->iii");
		clickElementById("ui_rmenu_privacy");
      
      //TODO verify, we are in the right menu. Here and in Listener
System.out.println("-->o");
		clickElementById("privacy_edit_photos_saved");
System.out.println("-->p");
		clickElementById("privacy_item3");
 System.out.println("-->a");
		clickElementById("privacy_edit_groups"); 
System.out.println("-->s");
		clickElementById("privacy_item3");
System.out.println("-->d");
		clickElementById("privacy_edit_wall_send");
System.out.println("-->f");
		clickElementById("privacy_item3"); 
System.out.println("-->g");
		clickElementById("privacy_edit_status_replies"); 
System.out.println("-->h");
		clickElementById("privacy_item3");
System.out.println("-->j");
		clickElementById("privacy_edit_mail_send");
System.out.println("-->k");
		clickElementById("privacy_item1");
System.out.println("-->l");
		clickElementById("privacy_edit_appscall");
System.out.println("-->z");
		clickElementById("privacy_item3"); 
System.out.println("-->x");
		clickElementById("privacy_edit_groups_invite"); 
System.out.println("-->c");
		clickElementById("privacy_item3");  
System.out.println("-->v");
		clickElementById("privacy_edit_apps_invite");
System.out.println("-->b");
		clickElementById("privacy_item3");
System.out.println("-->n");
	}
}