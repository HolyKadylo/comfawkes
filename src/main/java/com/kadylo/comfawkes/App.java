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

import java.net.MalformedURLException; 
import java.net.URL;
import org.openqa.selenium.Platform; 

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {		
		/* //(String email, 
		String password, 
		String phoneNo, 
		Node node, 
		Role role) */
		Account account = new Account("jokeprikol@rambler.ru", "prikol15", "0972594950", Account.Role.LISTENER);
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
		//
		// storage.put(Public.Media.VIDEO, "https://vk.com/videos-9761670?section=album_1");
		//
		// storage.put(Public.Media.AUDIO, "https://vk.com/audios-9761670");
		//
		Public pub00 = new Public("https://vk.com/thisiswhathappenslarry", me, null, 248, 150574507, Public.ListenerRole.ADMIN, storage00);
		Public pub0 = new Public ("https://vk.com/kadylosbooks", me, null, 249, 144898340, Public.ListenerRole.STANDALONE, storage0);
        // Public pub1 = new Public("https://vk.com/kadylosbooks", me, null, 250, 17, Public.ListenerRole.ADMIN, storage);
		// Public pub2 = new Public("https://vk.com/groundhog_day_for_every_day", me, null, 251, 18, Public.ListenerRole.ADMIN, storage2);
		
      try{
		/* listener.start(pub00);
		//listener.initialize(pub00);
		listener.post(me, "hi there");
		listener.stop();
		listener.sleep(1500); */
		
		listener.start(pub0);
		//listener.post(me, "зіга ой \nнігєр стой");
		
		boolean iterate = true;
		int i = 0;
		while (iterate){
			listener.read();
			listener.takeScreenshot(String.valueOf(i));
			i++;
			iterate = i > 5000 ? false : true;
			listener.sleep(5000);
		}
		listener.stop();
		/* poster.sleep(1500);
		poster.initialize(pub1);
		System.out.println("-->\n-->\n-->");
		System.out.println("-->AUDIO " + pub1.getMediaStorage(Public.Media.AUDIO));
		System.out.println("-->VIDEO " + pub1.getMediaStorage(Public.Media.VIDEO));
		System.out.println("-->PICTURE " + pub1.getMediaStorage(Public.Media.PICTURE));
		poster.sleep(1500);
		poster.stop(); */
		
		/* poster.start(pub2);
		poster.sleep(1500);
		poster.subscribe("https://vk.com/groundhog_day_for_every_day");
		poster.sleep(120000);
		poster.sleep(1500);
		poster.initialize(pub2);
		System.out.println("-->\n-->\n-->");
		System.out.println("-->AUDIO " + pub2.getMediaStorage(Public.Media.AUDIO));
		System.out.println("-->VIDEO " + pub2.getMediaStorage(Public.Media.VIDEO));
		System.out.println("-->PICTURE " + pub2.getMediaStorage(Public.Media.PICTURE));
		poster.sleep(1500);
		poster.stop(); */
		//listener.post("https://vk.com/kadylosbooks?w=wall-144898340_47", "НОН НОБІС ДОМІНЕ! АВЕ МАРІА! ДЕУС ВУЛЬТ!", Public.Media.PICTURE, "144898340_456239029");
		//listener.post(me, "Взвейтесь кострами синие ночи. Мы -- руснявые империалисты и красно-коричневые маньяки, Дети рабочих");
		//listener.sleep(6000);
      //  poster.takeScreenshot("res");
		/* poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_39", "NO< MEGATRON YOU SON OF A BITCH");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_26", "AVE CAESAR");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_26", "NO! HEIL CENATE");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_26", "SPQR FOR CAESAR");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_26", "[stabbing Caesar to death]");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_26", "et tu, Brutus?");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_26", "et [fataliity]");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_26", "AVE CAESAR");
		poster.sleep(1000); */
		/* poster.post("https://vk.com/the_god_machine_sect?w=wall-144898340_46", "Батько Бандера знов до нас прийде");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-144898340_46", "І за Україну в бій нас поведе");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-144898340_46", "Буде Україна сильна, буде ненька самостійна");
		poster.sleep(1000);
		poster.post("https://vk.com/the_god_machine_sect?w=wall-144898340_46", "Москалів на ножі, на ножі");
		poster.sleep(1000); */
		//poster.post("https://vk.com/the_god_machine_sect?w=wall-9761670_39", "AUTOBOTS WILL PREVAIL!");
		//poster.sleep(6000);
		
        //poster.setSettings();
		//System.out.println("-->" + System.currentTimeMillis());
		//poster.post("https://vk.com/wall-144898340_47", "Слава Україні! Героям слава!");
		//System.out.println("-->" + System.currentTimeMillis());
		//poster.sleep(10000);
		//listener.subscribe("https://vk.com/biletskiy_swag");
		//listener.sleep(2000);
        //listener.addToFriends("https://vk.com/holy_kadylo");
		//listener.sleep(3000);
		//listener.subscribe("https://vk.com/kpop.fanfiki");
        //System.out.println("-->inside bil: " + listener.isInside("https://vk.com/biletskiy_swag"));
        //listener.sleep(3000);
        //System.out.println("-->inside kpf: " + listener.isInside("https://vk.com/kpop.fanfiki"));
        //if(listener.isInside("https://vk.com/biletskiy_swag"))
		//	listener.leaveResource("https://vk.com/biletskiy_swag");
        //if(listener.isInside("https://vk.com/kpop.fanfiki"));
		//	listener.leaveResource("https://vk.com/kpop.fanfiki");
        //listener.leaveResource("https://vk.com/biletskiy_swag");
		//System.out.println("-->" + listener.isInside("https://vk.com/biletskiy_swag"));
		//System.out.println("-->" + listener.isClosedGroup("https://vk.com/kpop.fanfiki"));
		//poster.stop();
        } catch (NoSuchElementException nse){
			try{
				File scrFile = (File)(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
				FileUtils.copyFile(scrFile, new File("TESTCASEFAIL.png"));
			} catch (IOException ioe){
				System.out.println("-->file exception");
				ioe.printStackTrace();
			}
			nse.printStackTrace();
        }
		/* System.setProperty("webdriver.gecko.driver", "geckodriver"); 	
		DesiredCapabilities cap = DesiredCapabilities.firefox();
 		cap.setBrowserName("firefox"); 	
		cap.setCapability("marionette", true); 			
		URL url = null;
		try{
			url = new URL("http://localhost:5000/wd/hub");
		} catch (MalformedURLException e){
			System.out.println("-->" + e);
		}
		WebDriver driver = new RemoteWebDriver(url, cap); 	

        // And now use this to visit Google
        driver.get("http://www.google.com");
		
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");
        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("q"));

        // Enter something to search for
        element.sendKeys("Cheese!");

        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());
        
        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("cheese!");
            }
        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());
        
        //Close the browser
        driver.quit(); */
    }
}