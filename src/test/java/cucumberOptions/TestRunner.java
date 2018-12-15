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
		tags = {"@bmw_end2end"},
//		tags = {"@end2end_7,@end2end_8"},
//		plugin={"pretty","html:target/cucumber-html-report",}
		plugin={"com.cucumber.listener.ExtentCucumberFormatter:target/html/report.html"},
		monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests  {
	@AfterClass
	public static void setup() {
		Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
		//Reporter.setSystemInfo("Test User", System.getProperty("user.name"));
		Reporter.setSystemInfo("Test User", "Davit");
		Reporter.setSystemInfo("Operating System Type", System.getProperty("os.name").toString());
		Reporter.setSystemInfo("Web App Name", "Modal User Management");
		Reporter.setSystemInfo("Build version", "v 2.3");
		Reporter.setTestRunnerOutput("Cucumber reporting using Extent Config");
	}
}
