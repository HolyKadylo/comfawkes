package com.kadylo.comfawkes;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.Keys;

// This is browser endpoint
public class Poster extends Node{
	
	private final int MAX_ERRORS_COUNT = 5;
	private int errorCount = 0;
  
    // around 5 mins
    //TODO Math.rand()
	private static final long TAB_MIN_LIFE = 5 * 59 * 1090;
  
    // String -- tab handle, Long -- last accessed ms
    private volatile HashMap<String, Long> openTabs;
	
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
		try{
			for (String tab : driver.getWindowHandles()){		
				driver.switchTo().window(tab);
				sleep(350);
				boolean tabIsOpen = false;
				
				// here we're countering "?" difference
				if (driver.getCurrentUrl().contains("?")){
					tabIsOpen = driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("?")).equals(address);
				} else {
					tabIsOpen = driver.getCurrentUrl().equals(address);
				}
				if(tabIsOpen){
					
					// means it is already open
					isThereOpenTab = true;
					// renewing time
					openTabs.put(tab, System.currentTimeMillis());
					break;
				}
			}
		} catch (WebDriverException wde){
			
			// do nothing 
		} catch (Exception e){
			e.printStackTrace();
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
		System.out.println("-->Tab is opened");
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
	
	// TODO format
	// todo remove unnesessary hierarchy
	private void scrollDownAndClick(WebElement el){
		try{
			sleep(150);
			el.click();
		} catch (ElementNotInteractableException enie) {
			//System.out.println("-->catched " + enie.toString()); 
			
			// means any
			WebElement voidElement = driver.findElement(By.xpath("//body"));
			voidElement.sendKeys(Keys.PAGE_DOWN);
			scrollDownAndClick(el);
		} catch (Exception e){
			System.out.println("-->Exception occured in mediapost: " + e.toString());
		}
	}
	
	//post with media
	// TODO private?
	public void post (String addressee, String content, Public.Media media, String mediaURI){
		System.out.println("-->hello");
		String leaveAComment = "Leave a comment...";
		String postAsGroup = "Post as group";
	
		// adding media to the post
		switch (media){
			case PICTURE:  
				openTab(addressee);
				element = driver.findElement(By.id("reply_field-" + extractWallId(addressee)));
				scrollDownAndClick(element);
				sleep(5000); //1000
				//element = driver.findElement(By.cssSelector("a.ms_item.ms_item_photo_type_photo"));
				//element = driver.findElement(By.className("ms_item ms_item_photo _type_photo"));
				element = driver.findElement(By.xpath("//div[@id='reply_add_media_-" + extractWallId(addressee) + "']//a[@class='ms_item ms_item_photo _type_photo']"));
				//element = driver.findElement(By.xpath("//a[@class='ms_item ms_item_photo _type_photo']"));
				element.click();
				sleep(5000);//2500
				element = driver.findElement(By.xpath("//*[text() = 'Choose from community photos']"));
				element.click();
				sleep(5000);//1000
				//-9761670_245781876
				//*[@id='album" + + "?rev=1']/a
				element = driver.findElement(By.xpath("//*[@id='album" + pub.getMediaStorage(Public.Media.PICTURE).substring(20) + "?rev=1']/a/div/div"));
				//element = driver.findElement(By.xpath("//a[@onclick=\"if (cur.cancelClick) return (cur.cancelClick = false); return cur.chooseFromAlbum('" + pub.getMediaStorage(Public.Media.PICTURE).substring(20) + "');; return nav.go(this, event)\"]"));
				// "if (cur.cancelClick) return (cur.cancelClick = false); return cur.chooseFromAlbum('" + pub.getMediaStorage(Public.Media.PICTURE).substring(20) + "');; return nav.go(this, event)"
				// element = driver.findElement(By.xpath("//a[@data-href='album" + pub.getMediaStorage(Public.Media.PICTURE).substring(20) + "?rev=1']"));
				// ((JavascriptExecutor)driver).executeScript("cur.chooseFromAlbum('" + pub.getMediaStorage(Public.Media.PICTURE).substring(20) + "')");
				element.click();
				sleep(5000);//2500
				element = driver.findElement(By.id("photos_choose_row-" + mediaURI + "_" + pub.getMediaStorage(Public.Media.PICTURE).substring(20)));
				// <a id="photos_choose_row-9761670_456239024_-9761670_245781876" href="photo-9761670_456239024" 
				element.click();
				sleep(5000);//500
				break;
			case AUDIO:
				break;
			case VIDEO:
				break;
			default:
				break;
		}

		//ordinal posting
		post(addressee, content, true);
	}

	private String extractWallId(String address){
		
		// for example
		// https://vk.com/the_god_machine_sect?w=wall-9761670_39
		String result = address.substring(address.indexOf("?w=wall-")+1, address.length());
		String res2 = result.substring(result.indexOf("-")+1);
		return res2;
	}

	// posts content to the site's wall
	// addressee is wall address (that should be checked)
	// content should be checked as well
	// but all in the logic node
	// takes address?w=WALL-XXXXX-YY that needs to be extracted from user input!!!!!
	
	// does not depend on public
	// cascaded means that is called from media post
	public void post (String addressee, String content, boolean cascaded){
		System.out.println("-->Poster " + id + " is posting to " + addressee);
        try{
			if (!cascaded)
				openTab(addressee);
			String leaveAComment = "Leave a comment...";
			String postAsGroup = "Post as group";
			sleep(3500);
			element = driver.findElement(By.id("reply_field-" + extractWallId(addressee)));
			//element = driver.findElement(By.xpath("//*[text() = '" + leaveAComment + "']"));
            scrollDownAndClick(element);
			sleep(500); 
			WebElement commentBox = driver.findElement(By.id(makeReplyFieldId(addressee)));
			commentBox.sendKeys(content);
			sleep(3500);
			boolean groupActive = false;
			while (!groupActive){
				WebElement elementGroup = driver.findElement(By.id(makeRAGId(addressee)));
				elementGroup.click();
				sleep(900);
				if (isAttribtuePresentAndEqual(elementGroup, "aria-label", "post as community"))
					groupActive = true;
			}			
			element = driver.findElement(By.id(makeReplyButtonId(addressee)));
			element.click();
			sleep(3500);
			
			// closing tabs that live longer than 5 mins
			closeUnusedTabs();
			System.out.println("-->Poster " + id + " successfully posted");
		} catch (Exception e){
			errorCount++;
			if (errorCount <= MAX_ERRORS_COUNT){
				System.out.println("-->Error occured, retrying... " + e.toString());
				counterPopup();
				post(addressee, content, cascaded);
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