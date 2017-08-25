package com.kadylo.comfawkes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.TakesScreenshot;
import java.util.HashMap;
import java.util.ArrayList;
import java.net.MalformedURLException; 
import java.net.URL;
import org.openqa.selenium.Platform; 
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Hello world!
 *
 */
public class App{
	public static final String NESTOR_RMQ_ADDRESS = "pw6shba02icivg8e2uh6pw6shba";
	
	// represents running in JVM code
	public App (){

	}
	
	// represents object that runs in this JVM
	Object object;
	public Object getObject(){
		return object;
	}
	public void setObject(Object object){
		this.object = object;
	}
	
	// node
	// args[0] -- role of the application
	// args[1] -- email
	// args[2] -- password
	// args[3] -- publicAddress
	// args[4] -- publicId
	// args[5] -- host & port on host for selenium
	// args[6] -- RMQ cookie
	// args[7] -- port on host for RMQ
	// args[8] -- host for RMQ
	
	// nestor
	// args[0] -- role of the application
	// args[1] -- RMQ cookie
	// args[2] -- port on host for RMQ
	// args[3] -- host for RMQ
	
    public static void main( String[] args ){
		
		App app = new App();
		
		// WORKING NODE
		if (args[0].equals("listener") || args[0].equals("poster")){
			System.out.println("-->Linking to RMQ sender");
			int RMQport = 0;
			int publicId = 0;
			try{
				RMQport = Integer.parseInt(args[7]);
				publicId = Integer.parseInt(args[4]);
			} catch (NumberFormatException nfe){
				System.out.println("-->Error while parsing RMQ port or publicID: " + nfe.toString());
			}
			RabbitSender rabbitSender = new RabbitSender(args[8], RMQport);
			System.out.println("-->Linked to RMQ sender");
			
			System.out.println("-->Creating target class of " + args[0]);
			if (args[0].equals("listener")){
				Listener listener = new Listener(new Account(args[1], args[2], "0972594950", Account.Role.LISTENER), args[5], publicId, rabbitSender);
				app.setObject(listener);
			}
			if (args[0].equals("poster")){
				Poster poster = new Poster(new Account(args[1], args[2], "0972594950", Account.Role.POSTER), args[5], publicId);
				app.setObject(poster);
			}
			System.out.println("-->Target class created");
			
			System.out.println("-->Linking to RMQ receiver");
			String queueName = RandomStringUtils.randomAlphabetic(25);
			System.out.println("-->Created queueName of " + queueName + ", reporting it back to Nestor");
			try{
				if (args[0].equals("listener")){
					rabbitSender.send(NESTOR_RMQ_ADDRESS, "10+" + publicId + "+" + queueName);
				}
				if (args[0].equals("poster")){
					rabbitSender.send(NESTOR_RMQ_ADDRESS, "11+" + publicId + "+" + queueName);
				}
			} catch (Exception e){
				System.out.println("-->Exception while reporting back queueName to Nestor");
			}
			System.out.println("-->Reported back, now creating class");
			RabbitReceiver rabbitReceiver = new RabbitReceiver(app.getObject(), args[6], RMQport, queueName, args[8]);
			System.out.println("-->Class created, linked to RMQ receiver");
			
			System.out.println("-->Logging in, creating public");			
			Public pub2use = new Public(args[3], null, null, 0, publicId, Public.ListenerRole.ADMIN, null);
			System.out.println("-->Public created, starting");
			if (args[0].equals("listener")){
				((Listener)app.getObject()).start(pub2use);
				System.out.println("-->It's listener, starting operations");
				(new Thread((Listener)app.getObject())).start();
			}
			if (args[0].equals("poster")){
				((Poster)app.getObject()).start(pub2use);
			}
			System.out.println("-->Public created, logged in, reporting back the readiness to operate");
			try{
				rabbitSender.send(NESTOR_RMQ_ADDRESS, "12+" + queueName);
			} catch (Exception e){
				System.out.println("-->Exception while reporting active status " + e.toString());
			}
			System.out.println("-->Reported back. Now operating");
		}
		
		// NESTOR
		if (args[0].equals("nestor")){
			System.out.println("-->Linking to RMQ sender");
			int RMQport = 0;
			try{
				RMQport = Integer.parseInt(args[2]);
			} catch (NumberFormatException nfe){
				System.out.println("-->Error while parsing RMQ port: " + nfe.toString());
			}
			RabbitSender rabbitSender = new RabbitSender(args[3], RMQport);
			System.out.println("-->Linked to RMQ sender");
			
			System.out.println("-->Creating target class of " + args[0]);
			SimpleNestor nestor = new SimpleNestor();
			app.setObject(nestor);
			System.out.println("-->Target class created");
			
			System.out.println("-->Linking to RMQ receiver");			
			RabbitReceiver rabbitReceiver = new RabbitReceiver(app.getObject(), args[1], RMQport, NESTOR_RMQ_ADDRESS, args[3]);
			System.out.println("-->Linked to RMQ receiver");
		}
		
		if (!args[0].equals("nestor") && !args[0].equals("poster") && !args[0].equals("listener")){
			System.out.println("-->No valid role of the application provided, exiting");
			System.exit(-1);
		}
	}
}