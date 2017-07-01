package com.kadylo.comfawkes;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

// This is browser endpoint
public class Poster extends Node{
	
	private final int MAX_ERRORS_COUNT = 5;
	private int errorCount = 0;
  
    // around 5 mins
    //TODO Math.rand()
	private static final long TAB_MIN_LIFE = 30000;
  
    // String -- tab handle, Long -- last accessed ms
    private volatile HashMap<String, Long> openTabs;
	private WebElement element;
	
	public Poster(Account account, String sURL, int id){
		super(account, sURL, id);
		openTabs = new HashMap<String, Long>();
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
	
	// used in the next method
	// opens tab or creates a new one
	// renews time if used tab is accessed
	private void openTab(String address){
		System.out.println("-->Opening tab with address " + address);
		boolean isThereOpenTab = false;
		System.out.println("-->0");
		try{
			for (String tab : driver.getWindowHandles()){		
				System.out.println("-->1");
				driver.switchTo().window(tab);
				System.out.println("-->2");
				sleep(350);
				System.out.println("-->3");
				boolean tabIsOpen = false;
				System.out.println("-->4");
				// here we're countering "?" difference
				if (driver.getCurrentUrl().contains("?")){
					System.out.println("-->5");
					tabIsOpen = driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("?")).equals(address);
					System.out.println("-->6");
				} else {
					System.out.println("-->7");
					tabIsOpen = driver.getCurrentUrl().equals(address);
					System.out.println("-->8");
				}
				System.out.println("-->9");
				if(tabIsOpen){
					System.out.println("-->10");
					// means it is already open
					isThereOpenTab = true;
					System.out.println("-->11");
					// renewing time
					openTabs.put(tab, System.currentTimeMillis());
					System.out.println("-->12");
					break;
				}
			}
		} catch (WebDriverException wde){
				System.out.println("-->13");
			// do nothing 
		}
        if (!isThereOpenTab){

			// means we neead a new one
			((JavascriptExecutor)driver).executeScript("window.open('" + address + "', '" + address + "');");
			sleep(5000);
			List<String> handles = new ArrayList <String> (driver.getWindowHandles());
			for (String handle : handles){
				if(openTabs.containsKey(handle))
					continue;
				else{
					driver.switchTo().window(handle);
                    driver.get(address);
                    sleep(3500);
					openTabs.put(handle, System.currentTimeMillis());
					break;
				}
			}
        }
	}
	
	// used in the next method
	private void closeUnusedTabs(){
		
		//not closing if it is the only tab
		if (driver.getWindowHandles().size() <= 1)
			return;
		
		Iterator<String> tab = openTabs.keySet().iterator();
		
		// removing from opentabs
		while (tab.hasNext()){
			String keyToRemove = null;
			keyToRemove = tab.next();
			if (openTabs.get(keyToRemove) == null)
				continue;
			if (openTabs.get(keyToRemove) + TAB_MIN_LIFE <= System.currentTimeMillis()){
				driver.switchTo().window(keyToRemove);
				tab.remove();
				openTabs.remove(keyToRemove);
				driver.close();
				System.out.println("-->Removed outlived tab " + keyToRemove);
			}
		}
	}
	
	// posts content to the site's wall
	// addressee is wall address (that should be checked)
	// content should be checked as well
	// but all in the logic node
	// takes WALL-XXXXX-YY that needs to be extracted from user input
	public void post (String addressee, String content){
		System.out.println("-->Poster " + id + " is posting to " + addressee);
        try{
			openTab(addressee);
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
			
			// closing tabs that live longer than 5 mins
			closeUnusedTabs();
			System.out.println("-->Poster " + id + " successfully posted");
		} catch (Exception e){
			errorCount++;
			if (errorCount <= MAX_ERRORS_COUNT){
				System.out.println("-->Error occured, retrying...");
				counterPopup();
				post(addressee, content);
			} else {
				System.out.println("-->Error while posting");
				e.printStackTrace();
			}
		}
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
		System.out.println("-->Setting settings on Poster" + id);
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
			clickElementById("privacy_item1");
			clickElementById("privacy_edit_appscall");
			clickElementById("privacy_item3"); 
			clickElementById("privacy_edit_groups_invite"); 
			clickElementById("privacy_item3");  
			clickElementById("privacy_edit_apps_invite");
			clickElementById("privacy_item3");
			System.out.println("-->Successfully set settings on Poster " + id);
		} catch (Exception e){
			System.out.println("-->Error while setting settings on Poster");
			e.printStackTrace();
		}
	}
}