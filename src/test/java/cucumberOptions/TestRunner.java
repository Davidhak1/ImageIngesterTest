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
		tags = {"@bmw_smoke"},
//		plugin={"html:target/cucumber-html-report","pretty"}
		plugin={"com.cucumber.listener.ExtentCucumberFormatter:target/html/report.html"}
)
public class TestRunner extends AbstractTestNGCucumberTests  {
	@AfterClass
	public static void setup() {
		Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
		//Reporter.setSystemInfo("Test User", System.getProperty("user.name"));
		Reporter.setSystemInfo("Test User", "imademethink");
		Reporter.setSystemInfo("Operating System Type", System.getProperty("os.name").toString());
		Reporter.setSystemInfo("Web App Name", "Modal User Management");
		Reporter.setSystemInfo("Build version", "v 2.3");
		Reporter.setTestRunnerOutput("Cucumber reporting using Extent Config");
	}
}
