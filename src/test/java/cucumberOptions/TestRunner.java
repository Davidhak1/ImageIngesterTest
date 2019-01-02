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
//		tags = {"@Rest_Assured, @bmw_logics"},
//		tags = {"@Rest_Assured, @bmw_logics, @bmw_showcase"},
		tags = {"@bmw_showcase"},
		plugin={"com.cucumber.listener.ExtentCucumberFormatter:target/html/report.html"},
		monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests  {
	@AfterClass
	public static void setup() {
		Reporter.loadXMLConfig(new File("src/main/java/resources/extent-config.xml"));
		Reporter.setSystemInfo("Test User", "Davit Hakobyan");
		Reporter.setSystemInfo("Operating System Type", System.getProperty("os.name"));
        Reporter.setSystemInfo("OS version", System.getProperty("os.version"));
        Reporter.setSystemInfo("Web App Name", "Image Ingester");
		Reporter.setSystemInfo("Build version", "v 2.3");
		Reporter.setTestRunnerOutput("Cucumber reporting using Extent Config");
	}
}
