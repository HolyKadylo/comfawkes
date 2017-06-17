package com.kadylo.comfawkes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

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
		Node listener = new Listener(account, "https://localhost:5000", 10);
		account.setNode(listener);
		
		listener.start();
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