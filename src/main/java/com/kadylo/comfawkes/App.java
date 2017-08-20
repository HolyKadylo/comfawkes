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

/**
 * Hello world!
 *
 */
public class App {
	
	public App (){
		try{
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException uhe){
			System.out.println("-->Unknown host exception: " + uhe.toString());
			hostname = "unknown";
		}
		System.out.println("-->Hostname set to: " + hostname);
	}
	
	// node config that needs to be launched on this JVM
	class ArgsTask{
			
		private String approle;
		private String email = "";
		private String password = "";
		private String publicAddress = "";
		private int id = 0;
		private String RMQ_COOKIE = "";
		private int seleniumPort = 0;
		private int RMQPort = 0;
		
		ArgsTask(String approle, 
			String email, 
			String password, 
			String publicAddress, 
			int id, 
			String RMQ_COOKIE, 
			int seleniumPort, 
			int RMQPort){
				
			this.approle = approle;
			this.email = email;
			this.password = password;
			this.publicAddress = publicAddress;
			this.id = id;
			this.RMQ_COOKIE = RMQ_COOKIE;
			this.seleniumPort = seleniumPort;
			this.RMQPort = RMQPort;
			System.out.println("-->ArgsTask created: approle:" 
				+ approle 
				+ " email:" 
				+ email 
				+ " password:" 
				+ password 
				+ " id:" 
				+ id 
				+ " RMQ:" 
				+ RMQ_COOKIE 
				+ " seleniumPort:" 
				+ seleniumPort 
				+ " RMQPort:" 
				+ RMQPort);
		}
		
		public String getApprole(){
			return approle;
		}
		public String getEmail(){
			return email;
		}
		public String getPassword(){
			return password;
		}
		public String getPublicAddress(){
			return publicAddress;
		}
		public int getId(){
			return id;
		}
		public String getRMQCookie(){
			return RMQ_COOKIE;
		}
		public int getSeleniumPort(){
			return seleniumPort;
		}
		public int getRMQPort(){
			return RMQPort;
		}
	}
	
	
	private String hostname = "";
	
	// nodes that are running on this JVM
	private static ArrayList<Node> nodes;
	
	private String RMQ_COOKIE = "";
	private int seleniumPort = 0;
	private int RMQPort = 0;
	//node
	// args[0] -- role of the application
	// args[1] -- email
	// args[2] -- password
	// args[3] -- publicAddress
	// args[4] -- publicId
	// args[5] -- port on localhost for selenium
	// args[6] -- RMQ cookie
	
	// nestor
	// args[0] -- role of the application
	// args[1] -- RMQ cookie
	
    public static void main( String[] args ){
		System.out.println("-->Starting app's public static void main");
		
		// this represents this instance
		App app = new App();
		app.RMQ_COOKIE = args[0];
		try{
			app.seleniumPort = Integer.parseInt(args[1]);
			app.RMQPort = Integer.parseInt(args[2]);
		} catch (NumberFormatException nfe){
			System.out.println("-->Exception while parsing ports " + nfe.toString());
		}
		
		// this represents it's "tentacles"
		// using hostname as its unique queue name
		//RabbitReceiver appReceiver = new RabbitReceiver(app, app.RMQ_COOKIE, app.RMQPort, app.hostname);
		//RabbitSender appSender = new RabbitSender(app.RMQPort);
		
		ArrayList<ArgsTask> argsTasks = new ArrayList<ArgsTask>();
		try{
			int i = 0;
			while (true) {
				
				// if chunk starts with listener or poster, we are parsing entities
				if (args[i+3].equals("listener") || args[i+3].equals("poster")){
					int id = 0;
					try{
						id = Integer.parseInt(args[i+7]);
					} catch (NumberFormatException nfe){
						System.out.println("-->Error while parsing arguments: " + nfe.toString());
					}
					argsTasks.add(app.new ArgsTask(
						args[i+3], 
						args[i+4], 
						args[i+5], 
						args[i+6], 
						id, 
						app.RMQ_COOKIE, 
						app.seleniumPort, 
						app.RMQPort));
					i += 5;
				} else {
					if (args[i+3].equals("nestor")){
						
						// launching nestor
						System.out.println("-->Launching Nestor");
						SimpleNestor nestor = new SimpleNestor();
						nestor.act(app, app.RMQ_COOKIE);
						i++;
					} else {
						i++;
					}
				}
				/* ArgsTask(String approle, 
					String email, 
					String password, 
					String publicAddress, 
					int id, 
					String RMQ_COOKIE, 
					int seleniumPort, 
					int RMQPort){ */
				
			}
		} catch (ArrayIndexOutOfBoundsException aioobe){
			System.out.println("-->:" + aioobe.toString());
		}
		
		/* switch (approle){
			
			// node
			case NODE:
			System.out.println("-->This is Node");
			RabbitReceiver receiver = new RabbitReceiver(app, args[6], 0);//-------------
			System.out.println("-->Node starts to recieve RQM commands");
			try{
				receiver.startReceive();
			} catch (Exception e){
				System.out.println("-->Error while receiving RMQ commands by Node: " + e.toString());
			}
			System.out.println("-->Node started to receive RMQ commands");
			
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
			/* break;
			
			// Nestor
			case NESTOR:
				SimpleNestor nestor = new SimpleNestor();
				nestor.act(app, args[1]);
			break;
			
			default:
			
			System.out.println("-->This is default");
			
			break;
		} */
    }
	
	//1 role listener/poster
	//2 email foo@bar.com / telephoneNo which we treat the same way
	//3 password abc123
	//4 publicAddress https://vv.com/xxxx
	//5 publicId 1234567
	//6 port 5001
	
	public void start (String role, String email, String password, String publicAddress, String publicId, String port){
		System.out.println("-->App starts a node " + publicId);
		int newId = 0;
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
		int id;
		try{
			id = Integer.parseInt(publicId);
		} catch (NumberFormatException nfe){
			System.out.println("-->Not parsable publicId while stop");
			return;
		}
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
			id = Integer.parseInt(publicId);
		} catch (NumberFormatException nfe){
			System.out.println("-->Not parsable publicId while reboot");
			return;
		}
		/* switch (approle){
			case NODE:
				System.out.println("-->Attempting to reboot Node " + publicId);
				System.out.println("-->Finding proper Node");
				Node node2reboot = null;
				
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
				Public newPub = new Public(oldPub.getAddress(), null, null, 0, oldPub.getId(), oldPub.getRole(), null);
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
		} */
		System.out.println("-->App ended to reboot");
	}
}