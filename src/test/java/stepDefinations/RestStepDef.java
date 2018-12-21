package stepDefinations;

import com.sun.jna.platform.win32.Advapi32Util;
import cucumber.api.DataTable;
import cucumber.api.PendingException;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Vehicle;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.testng.Assert;

import resources.Queries;
import resources.ResponseHolder;

import java.util.*;

import static io.restassured.RestAssured.given;
import static resources.base.initProp;

public class RestStepDef{
    ResponseHolder responseHolder;
    Response response;
    RequestSpecification request;
    Map<String, Object> responseMap;
    private static Queries q;
    Map<String, String> body;
    List<String> bodyArray;
    protected static Vehicle restVehicle;
    private static List <Vehicle> vehiclesList;
    private String url;

    @Given("^Initialization")
    public void initialization() {
        initProp();
        request = RestAssured.with();
        System.out.println("\nINIT RESPONSE\n");
        q = new Queries();


    }

    @Given("^the server endpoint is (.+)$")
    public void theApiHostIsHttpsDsdIntBmwgroupComInventoryServerCosyEndpoint(String host) throws Throwable {
        this.url = host;
    }

    @Given("^the apis are up and running for (.+)$")
    public void the_apis_are_up_and_running_for(String url) throws Throwable {
        this.url = url;
        response = given().when().get(url);
        Assert.assertEquals(200, response.getStatusCode());
    }

    @When("^adding api path for get request (.+)$")                                                                     //REMOVE
    public void adding_api_path_for_the_request(String apiUrl) throws Throwable {
        this.url += apiUrl;
    }

    @When("^adding api endpoint for get request as /vin$")
    public void adding_api_path_for_the_request_as_vin() throws Throwable {

        this.url = this.url + '/'+ restVehicle.getVin();
    }

    @When("^adding api endpoint for get request with path based on provider (.+)$")
    public void addingApiEndpointForGetRequestWithProvider(String oem) throws Throwable {
       String provider = getProvider(oem);
        this.url = this.url + provider + "/uuid/"+ restVehicle.getUuid();
    }

    @When("^adding api path and body for post requst (.+) with below details")
    public void addingApiPathForPostRequstApiLocationLocationDetails(String api_url, DataTable dataTable) throws Throwable {

        this.url += api_url;

        this.body = new LinkedHashMap<String, String>();
        for (DataTableRow row : dataTable.getGherkinRows()) {
            this.body.put(row.getCells().get(0), row.getCells().get(1));
        }

    }

    @When("^adding to the body the array of the uuids$")
    public void addingBodyAsAnArrayOfTheUuidsOfThatArray() throws Throwable {
        this.bodyArray = mixStepDef.getUuidsList();
    }

    @And("^adding following headers$")
    public void iAddFollowingHeaders(DataTable dataTable) throws Throwable {

        for (DataTableRow row : dataTable.getGherkinRows()) {

            request.header(row.getCells().get(0), row.getCells().get(1));

        }

    }

    @When("adding following parameters")
    public void I_add_the_following_parameters(DataTable dataTable) {

        for (DataTableRow row : dataTable.getGherkinRows()) {
            request.param(row.getCells().get(0), row.getCells().get(1));
        }
    }

    @When("^adding uuid, provider, vin parameters for vehicle$")
    public void addingUuidProviderVinParametersFromVehicle() throws Throwable {
        String provider = getProvider(restVehicle.getOem());

        request.param("uuid",restVehicle.getUuid());
        request.param("provider",provider);
        request.param("vin",restVehicle.getVin());


    }

    @When("^perform the request$")
    public void and_perform_the_request() throws Throwable {

        System.out.println(this.url);
        response = request.given().when().get(this.url);
        responseHolder.setResponse(response);
    }

    @When("^get a random vehicle of (.+) with (.+) status and (\\d+) images mapped to it$")
    public void getARandomVehicleOfBmwWithCompleteStatusAndImagesMappedToIt(String oem, String status, int number) throws Throwable {
        vehiclesList = q.getVehiclesByOemStatusAndNumberOfImagesMapped(oem,status,number);
        Assert.assertNotNull(vehiclesList, String.format("No vehicle exist with criteria oem: %s, status: %s images mapped: %d",oem, status, number ));

        restVehicle = vehiclesList.get(new Random().nextInt(vehiclesList.size()));
        System.out.println("Random chosen vehicle: "+ restVehicle);

    }

    @When("^get a random vhicle with (.+) and (.+) status not removed$")
    public void getARandomVehicleWithAccountIdAndNotRemoved(String accountId, String status) throws Throwable {
        vehiclesList = q.getVehiclesByAccountIdStatusNotRemoved(accountId,status);
        restVehicle = vehiclesList.get(new Random().nextInt(vehiclesList.size()));
    }

    @And("^perform the post request$")
    public void andPerformThePostRequest() throws Throwable {
        response = request.given().contentType(ContentType.JSON).body(this.body).when().post(this.url);
        responseHolder.setResponse(response);
    }

    @And("^perform the post request sending an array$")
    public void andPerformThePostRequestSendingAnArray() throws Throwable {
        response = request.given().contentType(ContentType.JSON).body(this.bodyArray).when().post(this.url);
        responseHolder.setResponse(response);
    }

    @Then("^the response code should be (\\d+)$")
    public void the_response_code_should_be(int responseCode) throws Throwable {
        Assert.assertEquals(responseHolder.getResponseCode(), responseCode);
    }

    @Then("^I should see json response with pairs on the filtered (.+) node$")
    public void i_should_see_json_response_with_pairs_on_the_filtered_node(String filter, DataTable dataTable) throws Throwable {

        Map<String, String> query = new LinkedHashMap<String, String>();

        for (DataTableRow row : dataTable.getGherkinRows()) {
            query.put(row.getCells().get(0), row.getCells().get(1));
        }

        ObjectReader reader = new ObjectMapper().reader(Map.class);
        responseMap = reader.readValue(responseHolder.getResponseBody());
        System.out.println(responseMap);

        //if filter == $ => we should remain in the root of the Object
        if(!(filter.equals("$"))) {
            responseMap = (Map<String, Object>) responseMap.get(filter);
        }

        for (String key : query.keySet()) {
            Assert.assertTrue(responseMap.containsKey(key));
            Assert.assertEquals(query.get(key), responseMap.get(key).toString());
        }

    }

    @Then("^I should see json response with the array of (.+) than (\\d+) items on the filtered (.+) node$")
    public void iShouldSeeJsonResponseWithTheArrayOfItemsOnTheFilteredImageUrlsNode(String comparison, int length, String filter) throws Throwable {

        int actualLen = responseHolder.lengthOfArray(filter);

        if(comparison.equalsIgnoreCase("equal")){
            Assert.assertEquals(actualLen, length, String.format("The lengths supposed, but are not equal. expected: %d | actual: %d", length, actualLen));
        }
        else if(comparison.equalsIgnoreCase("less")){
            Assert.assertTrue(actualLen < length,String.format("The actual length supposed, but is not less than expected. actual: %d | expected: %d", length, actualLen));
        }
        else{
            Assert.assertTrue(actualLen > length, String.format("The actual length supposed, but is not greater than expected. actual: %d | expected: %d", length, actualLen));
        }
    }

    @And("^I should see json response with keys on the filtered (.+) node$")
    public void iShouldSeeJsonResponseWithKeysOnTheFiltered$Node(String filter, DataTable dataTable) throws Throwable {

        List<String> query = new LinkedList<String>();

        for (DataTableRow row : dataTable.getGherkinRows()) {
            query.add(row.getCells().get(0));
        }

        ObjectReader reader = new ObjectMapper().reader(Map.class);
        responseMap = reader.readValue(responseHolder.getResponseBody());
        System.out.println(responseMap);

        //if filter == $ => we should remain in the root of the Object
        if(!(filter.equals("$"))) {
            responseMap = (Map<String, Object>) responseMap.get(filter);
        }

        for (String key : query) {
            Assert.assertTrue(responseMap.containsKey(key));
        }
    }

    @And("^vehicle table should have equal number of vehicles for account in the (.+) as the server returns the filtered (.+) node$")
    public void vehicleTableShouldHaveEqualNumberOfVehiclesForAccountInThePathAsTheServerReturns(String path, String filter) throws Throwable {
        String accountId = StringUtils.substringBefore(path,"/");
        int dbLen = q.getNumberOfVehiclesByAccountIdNotRemoved(accountId);
        int serviceLen = responseHolder.lengthOfArray(filter);

        System.out.println(String.format("%nAccount: %s, DB: %d, API: %d",accountId, dbLen, serviceLen));
        Assert.assertEquals(dbLen, serviceLen ,  String.format("The length of array in the response and DB items do not match. response len: %d, DB len: %d",serviceLen, dbLen));
    }

    @Then("^vehicle table should have equal number of accounts in the db as in the response at the filtered (.+) node for (.+)$")
    public void vehicleTableShouldHaveEqualNumberOfAccountsInTheDbAsInTheResponseAtTheFiltered$Node(String filter, String oem) throws Throwable {
        int dbLen = q.getNumberOfAccountsByOem(oem);
        int serviceLen = responseHolder.lengthOfArray(filter);

        System.out.println(String.format("%nOem: %s, DB: %d, API: %d",oem, dbLen, serviceLen));
        Assert.assertEquals(dbLen, serviceLen ,  String.format("The length of array in the response and DB items do not match. response len: %d, DB len: %d",serviceLen, dbLen));

    }

    private String getProvider(String oem){
        return  (oem.equalsIgnoreCase("bmw"))?"BMW_STOCK_IMAGES":"AOA_STOCK_IMAGES";
    }



}
