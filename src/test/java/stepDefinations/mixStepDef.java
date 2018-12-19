package stepDefinations;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;
import resources.Queries;
import resources.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mixStepDef {

    private Queries q;
    private static List <Vehicle> vehiclesList;
    private static List<String> uuidList;


    @Given("^fetching (\\d+) random (.+) vehicles to a list$")
    public void fetchingRandomNotRemovedVehiclesToAList(int length, String message) throws Throwable {
        q = new Queries();
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


    public static List<String> getUuidsList() {
        uuidList = new ArrayList<String>();

        for (Vehicle v : vehiclesList) {
            uuidList.add(v.getUuid());
        }

        return uuidList;
    }
}
