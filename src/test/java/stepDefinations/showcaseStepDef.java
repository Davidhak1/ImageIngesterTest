package stepDefinations;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import model.Vehicle;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.ShowcaseSite;
import resources.Queries;
import resources.base;

import java.util.List;
import java.util.Random;


public class showcaseStepDef extends base {
    private static Vehicle showVehicle;
    private static int imagesQuantity;
    private static ShowcaseSite scs;


    @Given("^showcase initialize browser$")
    public void initializeTheBrowserWithChromeShowcase() throws Throwable {
       driver = initializeDriver();
    }

    @When("^Get a random (.+) vehicle that has (.+) number of images mapped to it in db$")
    public void getARandomOemVehicleThatHasQuantityNumberOfImagesMappedToItInDb(String oem, String quantity) throws Throwable {
        this.imagesQuantity = 0;
        List<Vehicle> vehicles;
       if(quantity.equalsIgnoreCase("max")){
           this.imagesQuantity = Integer.parseInt(oem.equalsIgnoreCase("bmw")?prop.getProperty("BMW_MAX"):prop.getProperty("AOA_MAX"));
           vehicles = q.getVehiclesByOemAndMappedImagesSpecificQuantity(oem, imagesQuantity);
       }
       else
       {
           vehicles = q.getVehiclesByOemAndStatusAndCreatedOnNotNull(oem, "none");
       }


       Assert.assertNotNull(vehicles, String.format("No vehicle exist with criteria oem: %s, images mapped: %d",oem,imagesQuantity ));

        showVehicle = vehicles.get(new Random().nextInt(vehicles.size()));
        System.out.println("Random showVehicle chosen: "+ showVehicle);

    }

    @And("^fill in the search textbox with the uuid of the vehicle$")
    public void fillInTheSearchTextboxWithTheUuidOfTheVehicle() throws Throwable {
        scs = new ShowcaseSite(driver);
        scs.getUuidInputBox().sendKeys(showVehicle.getUuid());
    }

    @And("^choose QA environment and press Locate$")
    public void chooseQAEnvironmentAndPressLocate() throws Throwable {
       scs.getQARadioBttn().click();
       scs.getLocateBttn().click();
    }

    @Then("^the vin of the showcase should be equal to the vehicle's vin$")
    public void theVinOfTheShowcaseShouldBeEqualToTheVehicleSVin() throws Throwable {
        String scsVin = scs.getVinValue().getText();
        Assert.assertEquals(showVehicle.getVin(),scsVin, String.format(" Vins are not equa. Showcase site vin: %s. Vehicles vin: %s",scsVin, showVehicle.getVin()));
    }

    @And("^vinSpecificStockImages should be equal to max number of images$")
    public void vinspecificstockimagesShouldBeEqualToMaxNumberOfImages() throws Throwable {
        int linksInShowcase = scs.getNumberOfStockImagesLinks();

        System.out.println(linksInShowcase + " : " + imagesQuantity) ;

        Assert.assertEquals(linksInShowcase,imagesQuantity,String.format("The quantity of images in showcase and for my vehicle don't match." +
                " Showcase: %d , Myvehicle: %d ",linksInShowcase, imagesQuantity));
    }


    @And("^Navigate showcase to \"([^\"]*)\"$")
    public void navigateShowcaseTo(String link) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        driver.get(link);
    }

    @Then("^showcase close browser$")
    public void initializeTheBrowserWithChromeForShowcase() throws Throwable {
        driver.close();
    }
}
