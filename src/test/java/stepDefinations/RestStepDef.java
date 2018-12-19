package stepDefinations;

import com.sun.jna.platform.win32.Advapi32Util;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import resources.Queries;
import resources.ResponseHolder;
import resources.base;

import javax.security.auth.login.AccountException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static resources.base.initProp;

public class RestStepDef {
    ResponseHolder responseHolder;
    Response response;
    RequestSpecification request;
    Map<String, Object> responseMap;
    Queries q;
    Map<String, String> body;
    List<String> bodyArray;
    private String url;

    @Given("^Initialization")
    public void initialization() {
        initProp();
        request = RestAssured.with();
        System.out.println("\nINIT RESPONSE\n");
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

    @When("^adding api path for get request (.+)$")
    public void adding_api_path_for_the_request(String apiUrl) throws Throwable {
        this.url += apiUrl;
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

    @When("^adding api path for get request <path>$")
    public void addingApiPathForGetRequestPath() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
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

    @When("^perform the request$")
    public void and_perform_the_request() throws Throwable {

        System.out.println(this.url);
        response = request.given().when().get(this.url);
        ResponseHolder.setResponse(response);
    }

    @And("^perform the post request$")
    public void andPerformThePostRequest() throws Throwable {
        response = request.given().contentType(ContentType.JSON).body(this.body).when().post(this.url);
        ResponseHolder.setResponse(response);
    }

    @And("^perform the post request sending an array$")
    public void andPerformThePostRequestSendingAnArray() throws Throwable {
        response = request.given().contentType(ContentType.JSON).body(this.bodyArray).when().post(this.url);
        ResponseHolder.setResponse(response);
    }

    @Then("^the response code should be (\\d+)$")
    public void the_response_code_should_be(int responseCode) throws Throwable {
        Assert.assertEquals(ResponseHolder.getResponseCode(), responseCode);
    }

    @Then("^I should see json response with pairs on the filtered (.+) node$")
    public void i_should_see_json_response_with_pairs_on_the_filtered_node(String filter, DataTable dataTable) throws Throwable {

        Map<String, String> query = new LinkedHashMap<String, String>();

        for (DataTableRow row : dataTable.getGherkinRows()) {
            query.put(row.getCells().get(0), row.getCells().get(1));
        }

        ObjectReader reader = new ObjectMapper().reader(Map.class);
        responseMap = reader.readValue(ResponseHolder.getResponseBody());
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

        int actualLen = ResponseHolder.lengthOfArray(filter);

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


    @And("^vehicle table should have equal number of vehicles for account in the (.+) as the server returns the filtered (.+) node$")
    public void vehicleTableShouldHaveEqualNumberOfVehiclesForAccountInThePathAsTheServerReturns(String path, String filter) throws Throwable {
        String accountId = StringUtils.substringBefore(path,"/");
        Queries q = new Queries();
        int dbLen = q.getNumberOfVehiclesByAccountIdNotRemoved(accountId);
        int serviceLen = ResponseHolder.lengthOfArray(filter);

        System.out.println(String.format("%nAccount: %s, DB: %d, API: %d",accountId, dbLen, serviceLen));
        Assert.assertEquals(dbLen, serviceLen ,  String.format("The length of array in the response and DB items do not match. response len: %d, DB len: %d",serviceLen, dbLen));
    }


    @Then("^vehicle table should have equal number of accounts in the db as in the response at the filtered (.+) node for (.+)$")
    public void vehicleTableShouldHaveEqualNumberOfAccountsInTheDbAsInTheResponseAtTheFiltered$Node(String filter, String oem) throws Throwable {
        Queries q = new Queries();
        int dbLen = q.getNumberOfAccountsByOem(oem);
        int serviceLen = ResponseHolder.lengthOfArray(filter);

        System.out.println(String.format("%nOem: %s, DB: %d, API: %d",oem, dbLen, serviceLen));
        Assert.assertEquals(dbLen, serviceLen ,  String.format("The length of array in the response and DB items do not match. response len: %d, DB len: %d",serviceLen, dbLen));


    }
}