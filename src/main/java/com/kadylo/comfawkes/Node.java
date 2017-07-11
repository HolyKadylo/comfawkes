package com.kadylo.comfawkes;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
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
	
	protected static final String ALBUM_NAME = "Fawkes album";
	protected static final String PLAYLIST_NAME = "Fawkes playlist";
	protected static final String VIDEO_ALBUM_NAME = "Fawkes album";
	protected static final String CLASS_WITH_PUBLIC_ID_1 = "ui_thumb_x_button";
	protected static final String CLASS_WITH_PUBLIC_ID_2 = "page_cover_image";
	protected static final String CLASS_TO_CREATE_PLAYLIST = "audio_page__main_tabs_btn audio_page__add_playlist_btn";
	protected static final String PLAYLIST_TITLE_ID = "ape_pl_name";
	
	protected WebDriver driver;
	protected DesiredCapabilities cap;
	protected WebElement element;
  
    // this is the public, the Node talks to
	protected Public pub;
  
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
	
	// TODO add setups
	// starters & stoppers
	public void start(Public pub){
		System.out.println("-->Starting node " + id);
		try{
			login();
		} catch (MissingResourceException mre){
			System.out.println("-->Breaking node");
			this.state = State.BROKEN;
			return;
		}
		setOwnStatus("-->Is up");
        this.pub = pub;
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
	
	// initialize inside the public
	// (++????)
	// Create album
	// create playlist
	// create video album
	// should be called on Listener
	// returns integer public id
	public int initialize (Public pub){
		System.out.println("-->Node " + this.id + " is starting initialization of storages in " + pub.getAddress());
		
		// this is vk's id, not our node's
		int id = 0;
		state = State.INITIALIZING;
		driver.get(pub.getAddress());
		sleep(1500);
		
		// finding public numerical id using few 
		// classes in HTML in cascade
		// depending on do we have image or not
		try{
			element = driver.findElement(By.className(CLASS_WITH_PUBLIC_ID_1));
			String value = element.getAttribute("onclick");
			try{
				id = Integer.parseInt(value.substring(value.indexOf("-") + 1, value.indexOf(",")));
			} catch (NumberFormatException nfe){
				System.out.println("-->Failed to set ID of public while initializing");
				nfe.printStackTrace();
			}
		} catch (Exception e){
			element = driver.findElement(By.className(CLASS_WITH_PUBLIC_ID_2));
			String value = element.getAttribute("href");
			try{
				id = Integer.parseInt(value.substring(value.indexOf("-") + 1, value.indexOf("_")));
			} catch (NumberFormatException nfe){
				System.out.println("-->Failed to set ID of public while initializing");
				nfe.printStackTrace();
			}
		}

		// crating picture album
		try{
			driver.get("https://vk.com/albums-" + String.valueOf(id));
			sleep(5000);
			element = driver.findElement(By.id("photos_add_album_btn"));
			element.click();
			sleep(1500);
			element = driver.findElement(By.id("new_album_title"));
			element.click();
			element.sendKeys(ALBUM_NAME);
			element = driver.findElement(By.id("album_only_check"));
			element.click();
			element = driver.findElement(By.xpath("//*[text() = 'Create album']"));
			element.click();
			sleep(6000);
			pub.setMediaStorage(Public.Media.PICTURE, driver.getCurrentUrl());
		} catch (Exception e){
			System.out.println("-->Failed to create picture album while initializing");
			e.printStackTrace();
			try{
				File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
				FileUtils.copyFile(scrFile, new File("picture.png"));
			} catch (IOException ioe){
				System.out.println("-->file exception");
				ioe.printStackTrace();
			}
		}

		// creating playlist
		try{
			driver.get(pub.getAddress());
			sleep(1500);
			driver.get("https://vk.com/audios-" + String.valueOf(id));
			sleep(5000);
			/* element = driver.findElement(By.className(CLASS_TO_CREATE_PLAYLIST));
			System.out.println("-->32");
			element.click(); */
			((JavascriptExecutor) driver).executeScript("AudioPage.editPlaylist(-" + String.valueOf(id) + ");");
			sleep(1500);
			element = driver.findElement(By.id(PLAYLIST_TITLE_ID));
			element.sendKeys(PLAYLIST_NAME);
			element = driver.findElement(By.xpath("//*[text() = 'Save']"));
			element.click();
			sleep(6000);
			driver.get("https://vk.com/audios-" + String.valueOf(id) + "?section=playlists");
			sleep(3000);
			element = driver.findElement(By.xpath("//*[text() = '" + PLAYLIST_NAME + "']"));
			element.click();
			sleep(5000);
			pub.setMediaStorage(Public.Media.AUDIO, driver.getCurrentUrl());
		} catch (Exception e){
			System.out.println("-->Failed to create playlist while initializing");
			e.printStackTrace();
			try{
				File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
				FileUtils.copyFile(scrFile, new File("audio.png"));
			} catch (IOException ioe){
				System.out.println("-->file exception");
				ioe.printStackTrace();
			}
		}

		// creating video album
		try{
			driver.get(pub.getAddress());
			sleep(2000);
			driver.get("https://vk.com/videos-" + String.valueOf(id));
			sleep(1500);
			element = driver.findElement(By.id("video_add_album_btn"));
			element.click();
			sleep(3500);
			element = driver.findElement(By.id("video_album_edit_title"));
			element.sendKeys(VIDEO_ALBUM_NAME);
			element = driver.findElement(By.xpath("//*[text() = 'Save']"));
			element.click();
			sleep(8000);
			pub.setMediaStorage(Public.Media.VIDEO, driver.getCurrentUrl());
		} catch (Exception e){
			System.out.println("-->Failed to create video album while initializing");
			e.printStackTrace();
			try{
				File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
				FileUtils.copyFile(scrFile, new File("video.png"));
			} catch (IOException ioe){
				System.out.println("-->file exception");
				ioe.printStackTrace();
			}
		}
		state = State.WORKING;
		pub.setId(id);
		return id;
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
	
  //debug method
  // todo format
  protected void takeScreenshot(String name){
    try{
      File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
      FileUtils.copyFile(scrFile, new File(name + ".png"));
    } catch (IOException ioe){
      System.out.println("-->Screenshot error");
      ioe.printStackTrace();
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
	
	// TODO
	protected void setProfilePicture(String picId){
	
		// deleting previous profile picture
		try{
			
		} catch (Exception e){
			
		}
	}
	
	protected void logout (){
		System.out.println("-->Logging out on node " + id);
		WebElement element = driver.findElement(By.id("top_profile_link"));
		sleep(2500);
		element.click();
		element = driver.findElement(By.id("top_logout_link"));
		sleep(2500);
		element.click();
		sleep(7000);
		System.out.println("-->Logged out");
	}
	
	// TODO
	protected void postToOwnWall (String content){
		System.out.println("-->postToOwnWall( " + content + " )");
	}
	
	// TODO
	protected void setOwnStatus (String status){
		System.out.println("-->setOwnStatus( " + status + " )");
	}
	
}