package com.kadylo.comfawkes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.CompositeAction;

// This is browser endpoint
public class Listener extends Node{
	
	private WebElement element;
    private String readTab;
    private String writeTab;
	
	public Listener(Account account, String sURL, int id){
		super(account, sURL, id);
        System.out.println("-->q");
        readTab = driver.getWindowHandle();
System.out.println("-->w");
        String s = Keys.chord(Keys.CONTROL, "t");
System.out.println("-->e");
        new CompositeAction().addAction(new Actions(driver).keyDown(Keys.CONTROL)).addAction(new Actions(driver).sendKeys("t")).addAction(new Actions(driver).keyUp(Keys.CONTROL)).perform();
        //new Actions(driver).sendKeys(s).perform();
System.out.println("-->r");
        writeTab = driver.getWindowHandle();
System.out.println("-->t");
        driver.switchTo().window(readTab);
System.out.println("-->y");
	}
	
	// posts message to site user in dialog
	public void post (User addressee, String content){
		
	}
	
	// reads something from site
	public String read(){
		String message = null;
		
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
	}
}