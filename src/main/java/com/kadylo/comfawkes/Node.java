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
		System.out.println("-->1");
		this.id = id;
		System.out.println("-->2");
		System.setProperty("webdriver.gecko.driver", "geckodriver"); 	
		System.out.println("-->3");
		cap = DesiredCapabilities.firefox();
		System.out.println("-->4");
 		cap.setBrowserName("firefox"); 	
		System.out.println("-->5");
		cap.setCapability("marionette", true);
		System.out.println("-->6");
		currentAccount = account;
		System.out.println("-->7");
		state = State.UNUSED;
		System.out.println("-->8");
		setURL(sURL);
		System.out.println("-->9");
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
		logout();
		setOwnStatus("-->Is down");
		state = State.UNUSED;
		System.out.println("-->Node " + id + " stopped");
	}
	
	// sURL is the selenium ip address, the node should 
	// talk to. Should be like http://xxx.xxx.xxx.xxx:yyyy
	public void setURL(String sURL){
		System.out.println("-->10");
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
		System.out.println("-->11");
		try{
			System.out.println("-->12");
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
		System.out.println("-->13");
		driver = new RemoteWebDriver(url, cap); 
		System.out.println("-->14");
		// after that browser is opened or reopened
	}
	
	private void login (){
		System.out.println("-->Logging in");
		driver.get("https://vk.com");
		WebElement element = driver.findElement(By.name("email"));
		element.sendKeys(currentAccount.getEmail());
		element = driver.findElement(By.name("pass"));
		element.sendKeys(currentAccount.getPassword());
		element.submit();
		
		// allowing page to load
		try{
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException ie){
			System.out.println("-->Interrupted");
		}
		
		if (driver.findElements( By.id("top_audio_layer_place") ).size() != 0){
			// means we've found it
			System.out.println("-->Successfully logged in");
		} else {
			for (int i = 1; i <= 15; i++){
				System.out.println("-->Waiting " + i + " s");
				try{
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException ie){
					System.out.println("-->Interrupted");
				}
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
	
	private void logout (){
		
	}
	
	private void postToOwnWall (String content){
		System.out.println("-->postToOwnWall( " + content + " )");
	}
	
	private void setOwnStatus (String status){
		System.out.println("-->setOwnStatus( " + status + " )");
	}
	
}