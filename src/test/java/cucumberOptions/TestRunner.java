package cucumberOptions;

import java.io.*;

import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.junit.AfterClass;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/features",
		glue="stepDefinations",
		tags = {"@first,@second"}
)
public class TestRunner extends AbstractTestNGCucumberTests  {

}
