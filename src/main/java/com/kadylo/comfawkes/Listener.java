package com.kadylo.comfawkes;

// This is browser endpoint
public class Listener extends Node{
	
	public Listener(Account account, String sURL, int id){
		super(account, sURL, id);
		
	}
	
	// posts message to site user in dialog
	public void post (User addressee, String content){
		
	}
	
	// reads something from site
	public String read(){
		String message = null;
		
		return message;
	}
	
	private void setSettings(){
		/* driver.get("https://vk.com/feed");
		sleep(1500);
		WebElement element = driver.findElement(By.id("top_profile_link"));
		element.click();
		sleep(1600);
		element = driver.findElement(By.id("top_settings_link"));
		element.click();
		sleep(1500); */
	}
}