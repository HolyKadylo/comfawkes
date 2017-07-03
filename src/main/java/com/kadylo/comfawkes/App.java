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
        Poster poster = new Poster(account, "http://localhost:5000", 10);
		account.setNode(poster);
        WebDriver driver = null;
        driver = poster.getDriver();
		
		User me = new User ("https://vk.com/holy_kadylo", 11, null, "Illya Piven", 150);
		HashMap <Public.Media, Boolean> allowed = new HashMap<Public.Media, Boolean>();
		
		// everything allowed
		PICTURE, VIDEO, AUDIO, GIF
		allowed.put(Public.Media.PICTURE, true);
		allowed.put(Public.Media.VIDEO, true);
		allowed.put(Public.Media.AUDIO, true);
		allowed.put(Public.Media.GIF, true);
		HashMap <Public.Media, String> storage = new HashMap<Public.Media, String>();
        Public pub1 = new Public("https://vk.com/the_god_machine_sect", me, null, 250, 17, Public.ListenerRole.ADMIN, allowed, storage);
		
      try{
		poster.start(pub1);
		poster.post("https://vk.com/wall-9761670_39", "ALL HEIL OPTIMUS PRIME!");
		poster.sleep(6000);
		poster.post("https://vk.com/wall-9761670_39", "NO< MEGATRON YOU SON OF A BITCH");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-9761670_26", "AVE CAESAR");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-9761670_26", "NO! HEIL CENATE");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-9761670_26", "SPQR FOR CAESAR");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-9761670_26", "[stabbing Caesar to death]");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-9761670_26", "et tu, Brutus?");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-9761670_26", "et [fataliity]");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-9761670_26", "AVE CAESAR");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-144898340_46", "Батько Бандера знов до нас прийде");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-144898340_46", "І за Україну в бій нас поведе");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-144898340_46", "Буде Україна сильна, буде ненька самостійна");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-144898340_46", "Москалів на ножі, на ножі");
		poster.sleep(1000);
		poster.post("https://vk.com/wall-9761670_39", "AUTOBOTS WILL PREVAIL!");
		poster.sleep(6000);
		poster.stop();
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