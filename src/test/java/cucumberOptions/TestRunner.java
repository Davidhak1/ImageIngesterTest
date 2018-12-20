package cucumberOptions;

import java.io.*;

import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.junit.AfterClass;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

import com.cucumber.listener.Reporter;


@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/features",
		glue="stepDefinations",
//		tags = {"@bmw_end2end"},
		tags = {"@Rest_Assured"},
//		tags = {"@bmw_end2end"},
//		plugin={"pretty","html:target/cucumber-html-report",}
		plugin={"com.cucumber.listener.ExtentCucumberFormatter:target/html/report.html"},
		monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests  {
	@AfterClass
	public static void setup() {
		Reporter.loadXMLConfig(new File("src/main/java/resources/extent-config.xml"));
		//Reporter.setSystemInfo("Test User", System.getProperty("user.name"));
		Reporter.setSystemInfo("Test User", "Davit Hakobyan");
		Reporter.setSystemInfo("Operating System Type", System.getProperty("os.name"));
//        Reporter.setSystemInfo("OS architecture", System.getProperty("os.arch"));
        Reporter.setSystemInfo("OS version", System.getProperty("os.version"));
//        Reporter.setSystemInfo("User working directory", System.getProperty("user.dir"));
//        Reporter.setSystemInfo("User home directory", System.getProperty("user.home"));
//        Reporter.setSystemInfo("User account name", System.getProperty("user.name"));



        Reporter.setSystemInfo("Web App Name", "Image Ingester");
		Reporter.setSystemInfo("Build version", "v 2.3");
		Reporter.setTestRunnerOutput("Cucumber reporting using Extent Config");
	}
}
