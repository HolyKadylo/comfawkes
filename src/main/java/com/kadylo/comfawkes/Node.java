package com.kadylo.comfawkes;

import org.openqa.selenium.WebDriver;
import java.net.MalformedURLException; 
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import java.lang.Thread;
import org.openqa.selenium.OutputType;
import java.io.File;
import org.apache.commons.io.FileUtils;
import java.util.MissingResourceException;
import org.openqa.selenium.TakesScreenshot;
import java.lang.InterruptedException;
import java.io.IOException;

// this class is used for basic selenium operations.
// Is parent to Poster and Listener
// This is browser endpoint
public class Node{
	
	protected WebDriver driver;
	protected DesiredCapabilities cap;
	
	// describes the node's state
	protected enum State{
		BROKEN, // when error forced to stop the logged in or logged out node
        WORKING, // when logged in node is active (with Nestor and ordinal logic node) in public or page
        READY, // when logged in node is ready for orders, but currently unused by logic node (i.e. hasn't one). Nestor sees ready nodes
        INITIALIZING, // when logged in node listens to initialization commands of separate Initialization logic node. Used to prove clients in first time config
        CREATED // when created and unlogged in
	}
	protected State state;
	protected Account currentAccount;
	
	// This is URL the node talks to
	// in constructor the part /wd/hub is added
	// to sURL
	protected URL url;
	
	// unique identifier
	protected int id;
  
    // TODO relocate
    public WebDriver getDriver(){
      return driver;
    }
	
	// constructor
	public Node (Account account, String sURL, int id){
		System.out.println("-->Constructing node");
		this.id = id;
		System.setProperty("webdriver.gecko.driver", "geckodriver"); 	
		cap = DesiredCapabilities.firefox();
 		cap.setBrowserName("firefox"); 	
		cap.setCapability("marionette", true);
		currentAccount = account;
		state = State.CREATED;
		setURL(sURL);
		System.out.println("-->Node " + id + " constructed");
	}
	
	// starters & stoppers
	public void start(){
		System.out.println("-->Starting node " + id);
		try{
			login();
		} catch (MissingResourceException mre){
			System.out.println("-->Breaking node");
			this.state = State.BROKEN;
			return;
		}
		setOwnStatus("-->Is up");
		state = State.READY;
		System.out.println("-->Node " + id + " started");
	}
  
	public void stop(){
		System.out.println("-->Stopping node " + id);
		setOwnStatus("-->Is down");
		logout();
		state = State.CREATED;
		System.out.println("-->Node " + id + " stopped");
	}
	
	// sURL is the selenium ip address, the node should 
	// talk to. Should be like http://xxx.xxx.xxx.xxx:yyyy
	public void setURL(String sURL){
		System.out.println("-->Setting URL " + sURL + " to node " + id);
		
		// meaning that driver needs to quit
		if (state != State.CREATED){
			try{
				driver.quit();
			} catch (Exception e){
				System.out.println(
					"-->Exception occured while setting URL: " 
					+ e.toString()
				);
				System.out.println("-->Continuing");
			}
		}
		try{
			url = new URL(sURL + "/wd/hub");
		} catch (MalformedURLException e){
			System.out.println(
				"-->Exception occured while parsing URL: " 
				+ e.toString()
			);
			System.out.println("-->Breaking node");
			this.state = State.BROKEN;
			return;
		}
		driver = new RemoteWebDriver(url, cap); 
		System.out.println("-->URL " + sURL + " was set to node " + id);
		// after that browser is opened or reopened
	}
	
	protected void login (){
		System.out.println("-->Logging in on node " + id);
		WebElement element = null;
		try{
			driver.get("https://vk.com");
			
			// countermeasures against popup
			counterPopup();
			
			sleep(5000);
			element = driver.findElement(By.id("index_email"));
			element.sendKeys(currentAccount.getEmail());
			element = driver.findElement(By.id("index_pass"));
			element.sendKeys(currentAccount.getPassword());
			element.submit();
		} catch (Exception e){
			throw new MissingResourceException("","","");
		}
		// allowing page to load
		sleep(5000);
		
		if (driver.findElements( By.id("top_audio_layer_place") ).size() != 0){
			// means we've found it
			System.out.println("-->Successfully logged in");
		} else {
			for (int i = 1; i <= 15; i++){
				System.out.println("-->Waiting " + i + " s");
				sleep(1000);
			}
			if (driver.findElements( By.id("top_audio_layer_place") ).size() != 0){
				// means we've found it
				System.out.println("-->Successfully logged in");
			} else {
				System.out.println("-->Login failed, making a screenshot");
				try{
					File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
					FileUtils.copyFile(scrFile, new File("NODE_" + id + "_LOGIN_SCREENSHOT.png"));
				} catch (IOException ioe){
					System.out.println("-->File exception: " + ioe.toString());
				}
				System.out.println("-->Quit now");
				driver.quit();
				System.out.println("-->Throwing an exception");
				throw new MissingResourceException("","","");
			}
		}
	}
	
	// TODO
	protected void register(){
		
	}
	
	protected void counterPopup(){
		// countermeasures against popup
		try{
			WebElement element = driver.findElement(By.xpath("//*[text() = 'Confirm']"));
			element.click();
			System.out.println("-->Confirmation popup occured & handled on node " + id);
		} catch (Exception e){
			// nothing, no popup
		}
	}
	
	// stops execution for a certain amount of ms
	protected void sleep (long i){
      
        // +-40%
        i = Math.round(i + 0.4 * i * (Math.random() * 2 - 1));
		try{
			Thread.currentThread().sleep(i);
		} catch (InterruptedException ie){
			System.out.println("-->Interrupted");
		}
	}
	
	
	// subscribes to some public
	protected void subscribe(String target){
		System.out.println("-->Subscribing to " + target + " on node " + id);
		WebElement element = null;
		try{
			driver.get(target);
			sleep(5000);
			
			// countermeasures against popup
			counterPopup();
			if (driver.findElements( By.id("join_button") ).size() != 0){
				// means it's a group
				element = driver.findElement(By.id("join_button"));
				element.click();
				sleep(1000);
			}
			if (driver.findElements( By.id("public_subscribe") ).size() != 0){
				// means it's a public
				element = driver.findElement(By.id("public_subscribe"));
				element.click();
				sleep(1000);
			}
			System.out.println("-->Successfully subscribed to " + target);
		} catch (Exception e){
			System.out.println("-->Failed to subscribe to " + target);
			e.printStackTrace();
		}
	}
  
    // is node banned on target resource?
    protected boolean isBanned(String target){
		driver.get(target);
		String bantext = "You have been banned from this community.";
		if (driver.findElements(By.xpath("//*[text() = '" + bantext + "']")).size() != 0){
			
			//means we're banned
			return true;
		} else {
			
			//means we're not
			return false;
		}
	}
  
    // is target a closed group?
    protected boolean isClosedGroup(String target){
		driver.get(target);
		String text = "This is a closed group";
		if (driver.findElements(By.xpath("//*[text() = '" + text + "']")).size() != 0){
			
			//means text is present, group is closed
			return true;
		} else {
			
			//means we're not
			return false;
		}
	}
  
    // sends request to closed group
    protected void sendRequest(String target){
		driver.get(target);
		try{
			WebElement element = driver.findElement(By.xpath("//*[text() = 'Send a request']"));
			element.click();
		} catch (Exception e){
			// nothing, no popup
		}
	}
  
    /* // approves invite to closed group
    private void approveInvite(String target){
		
	} */
	
	protected boolean isInside(String target){
		driver.get(target);
		String grouptext = "You are a member";
		String publicText = "Following";
		if (driver.findElements(By.xpath("//*[text() = '" + grouptext + "']")).size() != 0){
			
			//means we're in group
			return true;
		} else {
			if (driver.findElements(By.xpath("//*[text() = '" + publicText + "']")).size() != 0){
				
				//means we're in public
				return true;
			} else {
				
				//means we're not
				return false;
			}
		}
	}
	
    // public, group
	protected void leaveResource(String target){
        System.out.println("-->Leaving " + target + " on node " + id);
		driver.get(target);
		String grouptext = "You are a member";
		String leaveGroup = "Leave community";
		String leavePublic = "Unfollow";
		String publicText = "Following";
		WebElement element = null;
		if (driver.findElements(By.xpath("//*[text() = '" + grouptext + "']")).size() != 0){
			
			//means we're in group
			element = driver.findElement(By.xpath("//*[text() = '" + grouptext + "']"));
			element.click();
			sleep(1000);
			element = driver.findElement(By.xpath("//*[text() = '" + leaveGroup + "']"));
			element.click();
			sleep(1500);
			try{
				element = driver.findElement(By.xpath("//*[text() = '" + leaveGroup + "']"));
				element.click();
			} catch (Exception e){
				
				// do nothing 
			}
             System.out.println("-->Successfully left " + target);
		} else {
			if (driver.findElements(By.xpath("//*[text() = '" + publicText + "']")).size() != 0){
				
				//means we're in public
				element = driver.findElement(By.xpath("//*[text() = '" + publicText + "']"));
				element.click();
				sleep(1000);
				element = driver.findElement(By.xpath("//*[text() = '" + leavePublic + "']"));
				element.click();
                 System.out.println("-->Successfully left " + target);
			} else {
				System.out.println("Failed to leave " + target);
				//TODO Error
			}
		}
	}
	
    // send request to target
	// approve friend
	protected void addToFriends(String target){
		driver.get(target);
		try{
			String addText = "Add to friends";
			WebElement element = driver.findElement(By.xpath("//*[text() = '" + addText + "']"));
			element.click();
			sleep(2000);
		} catch (Exception e){
			
			//TODO error
		}
	}
	
	protected void removeFromFriends(String target){
		System.out.println("-->Removing from friends " + target + " on node " + id);
		try{
			driver.get(target);
			String removeText = "In your friend list";
			String totalRemove = "Remove from friends";
			WebElement element = driver.findElement(By.xpath("//*[text() = '" + removeText + "']"));
			element.click();
			sleep(800);
			element = driver.findElement(By.xpath("//*[text() = '" + totalRemove + "']"));
			element.click();
			System.out.println("-->Successfully removed from friends " + target);
		} catch (Exception e){
			System.out.println("-->Error while removing from friends " + target);
			e.printStackTrace();
		}
	}
	
	protected void hideInFriends(String target){
		System.out.println("-->Hiding in friends target " + target + " on node " + id);
		try{
			driver.get("https://vk.com/feed");
			sleep(1500);
			String save = "Save changes";
			WebElement element = driver.findElement(By.id("top_profile_link"));
			element.click();
			sleep(1600);
			element = driver.findElement(By.id("top_settings_link"));
			element.click();
			sleep(1500);
			element = driver.findElement(By.id("ui_rmenu_privacy"));
			element.click();
			sleep(1300);
			element = driver.findElement(By.id("privacy_friends_hide"));
			element.click();
			sleep(1200);
			element = driver.findElement(By.xpath("//*[text() = '" + getUserName(target) + "']"));
			element.click();
			sleep(1000);
			element = driver.findElement(By.xpath("//*[text() = '" + save + "']"));
			element.click();
			sleep(1500);
			System.out.println("-->Successfully hided in friends target " + target );
		} catch (Exception e){
			System.out.println("-->Error while hiding in friends");
			e.printStackTrace();
		}
	}
	
	protected String getUserName(String target){
		driver.get(target);
		sleep(2090);
		return driver.getTitle();
	}
	
	protected void setProfilePicture(String picId){
	
		// deleting previous profile picture
		try{
			
		} catch (Exception e){
			
		}
	}
	
	// set privacy to the proper pre-defined state
	// set language settings
    // TODO separate setSets() for listener and poster
	/* private void setSettings(){
		driver.get("https://vk.com/feed");
		sleep(1500);
		WebElement element = driver.findElement(By.id("top_profile_link"));
		element.click();
		sleep(1600);
		element = driver.findElement(By.id("top_settings_link"));
		element.click();
		sleep(1500);
	} */
	
	protected void logout (){
		System.out.println("-->Logging out on node " + id);
		WebElement element = driver.findElement(By.id("top_profile_link"));
		sleep(1000);
		element.click();
		element = driver.findElement(By.id("top_logout_link"));
		sleep(1000);
		element.click();
		sleep(7000);
		System.out.println("-->Logged out");
	}
	
	protected void postToOwnWall (String content){
		System.out.println("-->postToOwnWall( " + content + " )");
	}
	
	protected void setOwnStatus (String status){
		System.out.println("-->setOwnStatus( " + status + " )");
	}
	
}