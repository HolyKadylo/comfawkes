package com.kadylo.comfawkes;

// This is browser endpoint
public class Poster extends Node{
	
	public Poster(Account account, String sURL, int id){
		super(account, sURL, id);
		
	}
	
	// posts content to the site's wall
	// addressee is wall address (that should be checked)
	public void post (String addressee, String content){
		
	}
	
	private void setSettings(){
		driver.get("https://vk.com/feed");
		/* sleep(1500);
		WebElement element = driver.findElement(By.id("top_profile_link"));
		element.click();
		sleep(1600);
		element = driver.findElement(By.id("top_settings_link"));
		element.click();
		sleep(1500);  */
	}
}