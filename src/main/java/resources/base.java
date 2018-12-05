package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class base {

    public WebDriver driver;
    public static Properties prop;
    public Queries q;
    public Vehicle v;
    public Vehicle myVehicle;
    public String uuid;

    public WebDriver initializeDriver() throws IOException {

        q = new Queries();
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/java/resources/data.properties");

        prop.load(fis);
        String browserName = prop.getProperty("browser");
        System.out.println(browserName);

        if (browserName.equals("chrome")) {
            driver = new ChromeDriver();
            //execute in chrome driver

        } else if (browserName.equals("firefox")) {
            driver = new FirefoxDriver();
            //firefox code
        } else if (browserName.equals("IE")) {
//	IE code
            driver = new ChromeDriver(); // F*k IE, Hahaha
        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;

    }


}
