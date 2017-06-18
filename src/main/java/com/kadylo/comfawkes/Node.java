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
	
	private WebDriver driver;
	private DesiredCapabilities cap;
	
	// describes the node's state
	private enum State{
		BROKEN, ACTIVE, UNUSED
	}
	private State state;
	private Account currentAccount;
	
	// This is URL the node talks to
	// in constructor the part /wd/hub is added
	// to sURL
	private URL url;
	
	// unique identifier
	private int id;
	
	// constructor
	public Node (Account account, String sURL, int id){
		System.out.println("-->Constructing node");
		this.id = id;
		System.setProperty("webdriver.gecko.driver", "geckodriver"); 	
		cap = DesiredCapabilities.firefox();
 		cap.setBrowserName("firefox"); 	
		cap.setCapability("marionette", true);
		currentAccount = account;
		state = State.UNUSED;
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
		state = State.ACTIVE;
		System.out.println("-->Node " + id + " started");
	
		// TODO delete
		try{
			File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
			FileUtils.copyFile(scrFile, new File("SUCCESS_" + id + ".png"));
		} catch (IOException ioe){
			System.out.println("-->File exception: " + ioe.toString());
		}
	}
	public void stop(){
		System.out.println("-->Stopping node " + id);
		setOwnStatus("-->Is down");
		logout();
		state = State.UNUSED;
		System.out.println("-->Node " + id + " stopped");
	}
	
	// sURL is the selenium ip address, the node should 
	// talk to. Should be like http://xxx.xxx.xxx.xxx:yyyy
	public void setURL(String sURL){
		// meaning that driver needs to quit
		if (state != State.UNUSED){
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
		// after that browser is opened or reopened
	}
	
	private void login (){
		System.out.println("-->Logging in");
		WebElement element = null;
		try{
			driver.get("https://vk.com");
			
			// countermeasures against popup
			counterPopup();
			
			wait(5000);
			element = driver.findElement(By.id("index_email"));
			element.sendKeys(currentAccount.getEmail());
			element = driver.findElement(By.id("index_pass"));
			element.sendKeys(currentAccount.getPassword());
			element.submit();
		} catch (Exception e){
			throw new MissingResourceException("","","");
		}
		// allowing page to load
		wait(5000);
		
		if (driver.findElements( By.id("top_audio_layer_place") ).size() != 0){
			// means we've found it
			System.out.println("-->Successfully logged in");
		} else {
			for (int i = 1; i <= 15; i++){
				System.out.println("-->Waiting " + i + " s");
				wait(1000);
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
	
	private void register(){
		
	}
	
	private void counterPopup(){
		// countermeasures against popup
		try{
			WebElement element = driver.findElement(By.xpath("//*[text() = 'Підтвердити']"));
			element.click();
		} catch (Exception e){
			// nothing, no popup
		}
	}
	
	// stops execution for a certain amount of ms
	// TODO variative?
	private void wait (long i){
		try{
			Thread.currentThread().sleep(i);
		} catch (InterruptedException ie){
			System.out.println("-->Interrupted");
		}
	}
	
	
	// subscribes to some public
	// TODO private?
	public void subscribe(String target){
		System.out.println("-->Subscribing to " + target);
		WebElement element = null;
		try{
			driver.get(target);
			wait(5000);
			
			// countermeasures against popup
			counterPopup();
			if (driver.findElements( By.id("join_button") ).size() != 0){
				// means it's a group
				element = driver.findElement(By.id("join_button"));
				element.click();
				wait(1000);
			}
			if (driver.findElements( By.id("public_subscribe") ).size() != 0){
				// means it's a public
				element = driver.findElement(By.id("public_subscribe"));
				element.click();
				wait(1000);
			}
			System.out.println("-->Successfully subscribed to " + target);
		} catch (Exception e){
			System.out.println("-->Failed to subscribe to " + target);
			e.printStackTrace();
		}
		
	}
	
	private void unsubscribe(String target){
		
	}
	
	private void addToFriends(String target){
		
	}
	
	private void removeFromFriends(String target){
		
	}
	
	private void hideInFriends(String target){
		
	}
	
	private void setProfilePicture(String picId){
	
		// deleting previous profile picture
		try{
			
		} catch (Exception e){
			
		}
	}
	
	// set privacy to the proper pre-defined state
	// set language settings
	private void setSettings(){
		
	}
	
	private void logout (){
		System.out.println("-->Logging out");
		WebElement element = driver.findElement(By.id("top_profile_link"));
		wait(1000);
		element.click();
		element = driver.findElement(By.id("top_logout_link"));
		wait(1000);
		element.click();
		wait(7000);
		System.out.println("-->Logged out");
	}
	
	private void postToOwnWall (String content){
		System.out.println("-->postToOwnWall( " + content + " )");
	}
	
	private void setOwnStatus (String status){
		System.out.println("-->setOwnStatus( " + status + " )");
	}
	
}