package com.kadylo.comfawkes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

// This is browser endpoint
public class Poster extends Node{
	
	private WebElement element;
	
	public Poster(Account account, String sURL, int id){
		super(account, sURL, id);
		
	}
	
	// TODO remake in future?
	// RAG = Reply As Group
	private String makeRAGId(String s){
		
		// this is wall case
		String r = "reply_as_group-" + s.substring(s.indexOf("-") + 1);
		
		// TODO delete
		System.out.println("-->r = " + r);
		return r;
	}
	
	//
	private String makeReplyFieldId(String s){
		
		// this is wall case
		String r = "reply_field-" + s.substring(s.indexOf("-") + 1);
		
		// TODO delete
		System.out.println("-->r = " + r);
		return r;
	}
	
	private String makeReplyButtonId(String s){
		
		// this is wall case
		String r = "reply_button-" + s.substring(s.indexOf("-") + 1);
		
		// TODO delete
		System.out.println("-->r = " + r);
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
	public void post (String addressee, String content){
		driver.get(addressee);
		String leaveAComment = "Leave a comment...";
		sleep(5000);
		System.out.println("-->1");
		element = driver.findElement(By.xpath("//*[text() = '" + leaveAComment + "']"));
		element.click();
		sleep(5000); 
		WebElement commentBox = driver.findElement(By.id(makeReplyFieldId(addressee)));
		System.out.println("-->2");
		commentBox.sendKeys(content);
		System.out.println("-->3");
		sleep(3000);
		WebElement elementGroup = driver.findElement(By.id(makeRAGId(addressee)));
		System.out.println("-->4");
		sleep(1540);
		System.out.println("-->4.5");
		elementGroup.click();
		while (!isAttribtuePresentAndEqual(elementGroup, "aria-checked", "false")){
			sleep(1950);
			System.out.println("-->5");
			elementGroup.click();
			System.out.println("-->6");
			System.out.println("clicked element group");
		}
		sleep(1100);
		System.out.println("-->7");
		element = driver.findElement(By.id(makeReplyButtonId(addressee)));
		System.out.println("-->8");
		sleep(1000);
		System.out.println("-->9");
		element.click();
		System.out.println("-->10");
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