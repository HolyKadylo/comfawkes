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

/**
 * Hello world!
 *
 */
public class App {
	private static enum Approle{
		NESTOR,NODE
	}
	private static Approle approle;
	private static ArrayList<Node> nodes;
	
	// args[0] -- role of the application
	// args[1] -- email
	// args[2] -- password
	// args[3] -- publicAddress
	// args[4] -- publicId
	// args[5] -- port on localhost for selenium
	// args[6] -- RMQ cookie
	
    public static void main( String[] args ){
		//String args are:
		// node -- for node testcase
		// nestor -- for being a Nestor
		if (args[0].equals("node"))
			approle = Approle.NODE;
		if (args[0].equals("nestor"))
			approle = Approle.NESTOR;
		
		switch (approle){
			
			// node
			case NODE:
			System.out.println("-->This is Node");
			RabbitReceiver receiver = new RabbitReceiver(this, args[6]);
			System.out.println("-->Node starts to recieve");
			receiver.startReceive();
			System.out.println("-->Node started to receive");
			
			/* // we think that we don't need telephone
			Account account = new Account(args[1], args[2], "0972594950", Account.Role.LISTENER);
			Listener listener = new Listener(account, "http://localhost:5000", 10);
			account.setNode(listener);
			WebDriver driver = null;
			driver = listener.getDriver();
			
			User me = new User ("https://vk.com/holy_kadylo", 12585304, null, "Illya Piven", 150);
			
			// everything allowed
			HashMap <Public.Media, String> storage00 = new HashMap<Public.Media, String>();
			HashMap <Public.Media, String> storage0 = new HashMap<Public.Media, String>();
			HashMap <Public.Media, String> storage = new HashMap<Public.Media, String>();
			HashMap <Public.Media, String> storage2 = new HashMap<Public.Media, String>();
			
			
			storage00.put(Public.Media.PICTURE, "https://vk.com/album-150574507_245779604");

			Public pub00 = new Public("https://vk.com/thisiswhathappenslarry", me, null, 248, 150574507, Public.ListenerRole.ADMIN, storage00);
			Public pub0 = new Public ("https://vk.com/kadylosbooks", me, null, 249, 144898340, Public.ListenerRole.STANDALONE, storage0);
			
		  try{
			listener.start(pub00);
			
			boolean iterate = true;
			int i = 0;
			while (iterate){
				listener.read();
				// listener.takeScreenshot(String.valueOf(i));
				i++;
				iterate = i > 5000 ? false : true;
				listener.sleep(5000);
			}
			listener.stop();
			
			} catch (Exception nse){
				try{
					File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
					FileUtils.copyFile(scrFile, new File("TESTCASEFAIL.png"));
				} catch (IOException ioe){
					System.out.println("-->file exception");
					ioe.printStackTrace();
				}
				nse.printStackTrace();
			} */
			break;
			
			// Nestor
			case NESTOR:
				SimpleNestor nestor = new SimpleNestor();
				nestor.act();
			break;
			
			default:
			
			System.out.println("-->This is default");
			
			break;
		}
    }
	
	//1 role listener/poster
	//2 email foo@bar.com / telephoneNo which we treat the same way
	//3 password abc123
	//4 publicAddress https://vv.com/xxxx
	//5 publicId 1234567
	//6 port 5001
	
	public void start (String role, String email, String password, String publicAddress, String publicId, String port){
		System.out.println("-->App starts a node " + publicId);
		int newId;
		try{
			newId = Integer.parseInt(publicAddress);
		} catch (NumberFormatException nfe){
			System.out.println("-->Error while parsing publicAddress at start");
		}
			/*Account account = new Account(email, password, "0972594950", Account.Role.LISTENER);
			Listener listener = new Listener(account, "http://localhost:5000", 10);
			account.setNode(listener);
			WebDriver driver = null;
			driver = listener.getDriver();
			
			User me = new User ("https://vk.com/holy_kadylo", 12585304, null, "Illya Piven", 150);
			
			// everything allowed
			HashMap <Public.Media, String> storage00 = new HashMap<Public.Media, String>();
			HashMap <Public.Media, String> storage0 = new HashMap<Public.Media, String>();
			HashMap <Public.Media, String> storage = new HashMap<Public.Media, String>();
			HashMap <Public.Media, String> storage2 = new HashMap<Public.Media, String>();
			
			
			storage00.put(Public.Media.PICTURE, "https://vk.com/album-150574507_245779604");

			Public pub00 = new Public("https://vk.com/thisiswhathappenslarry", me, null, 248, 150574507, Public.ListenerRole.ADMIN, storage00);
			Public pub0 = new Public ("https://vk.com/kadylosbooks", me, null, 249, 144898340, Public.ListenerRole.STANDALONE, storage0);
			
		  try{
			listener.start(pub00);
			
			boolean iterate = true;
			int i = 0;
			while (iterate){
				listener.read();
				// listener.takeScreenshot(String.valueOf(i));
				i++;
				iterate = i > 5000 ? false : true;
				listener.sleep(5000);
			}
			listener.stop();
			
			} catch (Exception nse){
				try{
					File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
					FileUtils.copyFile(scrFile, new File("TESTCASEFAIL.png"));
				} catch (IOException ioe){
					System.out.println("-->file exception");
					ioe.printStackTrace();
				}
				nse.printStackTrace();
			} */
		
		
		HashMap <Public.Media, String> storage00 = new HashMap<Public.Media, String>();
		storage00.put(Public.Media.PICTURE, "https://vk.com/album-150574507_245779604");
		
		
		User me = new User ("https://vk.com/holy_kadylo", 12585304, null, "Illya Piven", 150);
		Public public2use = new Public(publicAddress, me, null, 0, newId, Public.ListenerRole.ADMIN, storage00);
		WebDriver driver = null;
		if (role.equals("listener")){
			Account account = new Account(email, password, "0972594950", Account.Role.LISTENER);
			Listener listener = new Listener(account, "http://localhost:" + port, newId);
			account.setNode(listener);
			driver = listener.getDriver();
			
			nodes.add(listener);
		}
		if (role.equals("poster")){
			Account account = new Account(email, password, "0972594950", Account.Role.POSTER);
			Poster poster = new Poster(account, "http://localhost:" + port, newId);
			account.setNode(poster);
			driver = poster.getDriver();
			
			nodes.add(poster);
		}
	}
	
	public void stop (String publicId){
		System.out.println("-->App stops a node " + publicId);
		System.out.println("-->Attempting to stop Node " + publicId);
		System.out.println("-->Finding proper Node");
		for (Node node : nodes){
			if (node.getId() == id){
				node.stop();
				nodes.remove(node);
				break;
			}
		}
		System.out.println("-->Node " + publicId + " stopped");
	}
	
	public void reboot (String publicId){
		System.out.println("-->App a reboot process");
		int id;
		try{
			id = Integer.parseInt();
		} catch (NumberFormatException nfe){
			System.out.println("-->Not parsable publicId while reboot");
			return;
		}
		switch (approle){
			case NODE:
				System.out.println("-->Attempting to reboot Node " + publicId);
				System.out.println("-->Finding proper Node");
				Node node2reboot;
				
				// finding proper Node
				for (Node node : nodes){
					if (node.getId() == id){
						node2reboot = node;
						nodes.remove(node);
						break;
					}
				}
				System.out.println("-->Proper Node found. Stopping it");
				node2reboot.stop();
				node2reboot.sleep(10000);
				System.out.println("-->Node " + publicId + " stopped. Starting it again");
				Account oldAcc = node2reboot.getCurrentAccount();
				
				if (node2reboot instanceof Listener){
					System.out.println("-->node instanceof listener");
					Listener listener = new Listener(oldAcc, node2reboot.getSUrl(), node2reboot.getId());
					oldAcc.setNode(listener);
					WebDriver driver = null;
					driver = listener.getDriver();
				}
				if (node2reboot instanceof Poster){
					System.out.println("-->node instanceof poster");
					Poster poster = new Poster(oldAcc, node2reboot.getSUrl(), node2reboot.getId());
					oldAcc.setNode(poster);
					WebDriver driver = null;
					driver = poster.getDriver();
				}
				
				Public oldPub = node2reboot.getPublic();
				Public newPub = new Public(oldPub.getAdddress(), null, null, null, oldPub.getId(), oldPub.getRole(), null);
				node2reboot.start(newPub);
				nodes.add(node2reboot);
				System.out.println("-->Node " + publicId + " started again");
			break;
			case NESTOR:
				System.out.println("-->Attempting to reboot Nestor, declining");
			break;
			default:
				System.out.println("-->Default reboot, declining");
			break;
		}
		System.out.println("-->App ended to reboot");
	}
}