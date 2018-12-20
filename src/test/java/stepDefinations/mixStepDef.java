package stepDefinations;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import model.DownloadedImage;
import org.testng.Assert;
import resources.Queries;
import model.Vehicle;
import resources.Utils;

import java.util.*;

public class mixStepDef {

    private static Queries q;
    private static List <Vehicle> vehiclesList;
    private static List <DownloadedImage> dwnImages;

    public static List<String> getUuidsList() {
        List<String> uuidList = new ArrayList<String>();

        for (Vehicle v : vehiclesList) {
            uuidList.add(v.getUuid());
        }

        return uuidList;
    }

    @Given("^Queries Init in mix$")
    public void initializationOfQueriesInMix() throws Throwable {
        q = new Queries();
    }

    @Given("^fetching (\\d+) random (.+) vehicles to a list$")
    public void fetchingRandomNotRemovedVehiclesToAList(int length, String message) throws Throwable {
        boolean removed = false;

        if (message.equalsIgnoreCase("removed")) {
            removed = true;
        }

        List<Vehicle> allVehicles = q.getVehiclesByRemoved(removed);
        System.out.println("Total number of not removed vehicles: " + allVehicles.size());
        vehiclesList = new ArrayList<>();

        boolean repeat = true;

        while( (vehiclesList.size() < length))
        {
            vehiclesList.add(allVehicles.get(new Random().nextInt(allVehicles.size())));
        }


    }

    @Then("^all the vehicles in the list should have removed set to true$")
    public void allTheVehiclesInTheListShouldHaveRemovedSetToTrue() throws Throwable {
        for(Vehicle v: vehiclesList)
        {
            v=q.getVehicleByUUID(v.getUuid());
            Assert.assertTrue(v.isRemoved(), String.format("Failed to change removed to true for uuid: %s", v.getUuid()));
            System.out.println(String.format("%nremoved = %b for uuid: %s", v.isRemoved(), v.getUuid()));
        }
        System.out.println("\n\n\n");
    }

    @And("^set removed to false for all the vehicles and validate it$")
    public void setRemovedToFalseForAllTheVehicles() throws Throwable {
        for(Vehicle v: vehiclesList)
        {
            q.updateVehicleSetRemovedToFalse(v.getUuid());
            v = q.getVehicleByUUID(v.getUuid());
            Assert.assertTrue(!v.isRemoved(), String.format("Failed to rechange removed to false for uuid: %s", v.getUuid()));
            System.out.println(String.format("%nremoved = %b for uuid: %s", v.isRemoved(), v.getUuid()));

        }
    }

    @And("^Query all downloaded images of the vehicle in the db$")
    public void queryAllDownloadedImagesOfTheVehicleInTheDb() throws Throwable {

        String uuid = RestStepDef.restVehicle.getUuid();
        dwnImages = q.getDownloadedImagesByUuidAndNotRemoved(uuid);
        Assert.assertTrue(dwnImages!=null, "No downloaded images found for uuid: "+ uuid);

    }


    @And("^The priorities of the images should be correct$")
    public void thePrioritiesOfTheImagesShouldBeCorrect() throws Throwable {
        Map<Integer, String> priorities = Utils.getPriorities();
        System.out.println();
        int count = 0;
        for(DownloadedImage image: dwnImages)
        {
            String externalUrl = image.getExternalUrl();
            int image_pr = image.getPriority();
            Assert.assertTrue(externalUrl.contains(priorities.get(image_pr)), String.format("The image has other priority." +
                    " Url should contain: %s.%nActual external url: %s ",priorities.get(count), externalUrl  ));
            System.out.println(count + ": "+ priorities.get(count));
            count++;
        }

    }

}
