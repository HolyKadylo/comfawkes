package com.kadylo.comfawkes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.HashMap;

// This is browser endpoint
public class Poster extends Node{
	
  //TODO
    // String -- tab handle, Long -- last accessed
    private HashMap<String, Long> openTabs;
	private WebElement element;
	
	public Poster(Account account, String sURL, int id){
		super(account, sURL, id);
		
	}
	
	// RAG = Reply As Group
	private String makeRAGId(String s){
		String r = "reply_as_group-" + s.substring(s.indexOf("-") + 1);
		return r;
	}
	private String makeReplyFieldId(String s){
		String r = "reply_field-" + s.substring(s.indexOf("-") + 1);
		return r;
	}
	private String makeReplyButtonId(String s){
		String r = "reply_button-" + s.substring(s.indexOf("-") + 1);
		return r;
	}
	
	//https://stackoverflow.com/questions/20645013/how-can-i-verify-an-attribute-is-present-in-an-element
	private boolean isAttribtuePresentAndEqual(WebElement element, String attribute, String equal) {
		boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null){
				if (value.equals(equal)){
					result = true;
				}
			}
		} catch (Exception e) {
			
			//??
			System.out.println("--> Exception" + e.toString());
		}
		return result;
    }
	
	// posts content to the site's wall
	// addressee is wall address (that should be checked)
	// content should be checked as well
	// but all in the logic node
	// takes WALL-XXXXX-YY that needs to be extracted from user input
	public void post (String addressee, String content){
		driver.get(addressee);
		String leaveAComment = "Leave a comment...";
		String postAsGroup = "Post as group";
		sleep(3500);
		element = driver.findElement(By.xpath("//*[text() = '" + leaveAComment + "']"));
		element.click();
		sleep(500); 
		WebElement commentBox = driver.findElement(By.id(makeReplyFieldId(addressee)));
		commentBox.sendKeys(content);
		sleep(3500);
		WebElement elementGroup = driver.findElement(By.id(makeRAGId(addressee)));
		elementGroup.click();
		sleep(400);
		element = driver.findElement(By.xpath("//*[text() = '" + postAsGroup + "']"));
		element.click();
		sleep(400); 
		element = driver.findElement(By.id(makeReplyButtonId(addressee)));
		element.click();
		sleep(3500);
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
		clickElementById("privacy_item1");
		clickElementById("privacy_edit_appscall");
		clickElementById("privacy_item3"); 
		clickElementById("privacy_edit_groups_invite"); 
		clickElementById("privacy_item3");  
		clickElementById("privacy_edit_apps_invite");
		clickElementById("privacy_item3");
	}
}