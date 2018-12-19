//package stepDefinations;
//
//import cucumber.api.java.en.And;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import io.restassured.specification.ResponseSpecification;
//import org.apache.commons.codec.binary.Base64;
//import org.testng.Assert;
//import resources.base;
//
//import static io.restassured.RestAssured.given;
//import static io.restassured.RestAssured.when;
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.core.Is.is;
//
//
//public class stepDefinitionRestAssured extends base {
//
//    static String baseURL;
//
//
//    @Then("^Verify we are getting (\\d+) urls for the (.+) vin")
//    public void verify_we_are_getting_spesicic_urls_for_vin(int number, String vin) throws Throwable {
//
////        RestAssured.baseURI = prop.getProperty("SULZER_URI");
////
////        Response response = given().
////                header("user", "user").
////                header("password", "password").
////                when().
////                get("/"+vin+"?pov=driverdoor,dashboard&angle=45,90,135,180,225,270,315,360&bkgnd=transparent&imgtype=png&height=960&width=1280").
////                then().assertThat().statusCode(200).and().
////                contentType(ContentType.JSON).and().
////                body("imageUrls.size()", is(number)).extract().response();
////
////
////        Assert.assertEquals(response.statusCode(), 200);
////        Assert.assertEquals(response.jsonPath().getList("imageUrls").size(), 10);
//
//        RestAssured.baseURI = prop.getProperty("SULZER_URI");
//
//        RequestSpecification request;
//        request = RestAssured.with();
//
//        request.given().header("user","user").header("password","password");
//
//
//        Response response = request.
//                when().
//                get("/"+vin+"?pov=driverdoor,dashboard&angle=45,90,135,180,225,270,315,360&bkgnd=transparent&imgtype=png&height=960&width=1280").
//                then().assertThat().statusCode(200).and().
//                contentType(ContentType.JSON).and().
//                body("imageUrls.size()", is(number)).extract().response();
//
//
//        Assert.assertEquals(response.statusCode(), 200);
//        Assert.assertEquals(response.jsonPath().getList("imageUrls").size(), 10);
//
//    }
//
//
//    @Given("^set base url is for sulzer$")
//    public void base_url_is_for_sulzer() throws Throwable {
//
//    }
//
//    @When("^call (.+) with (.+) and right headers$")
//    public void call_inventoryservercosyendpoint_with_and_right_headers(String uri, String vin) throws Throwable {
//    }
//
//    @Then("^fetch the response$")
//    public void fetch_the_response() throws Throwable {
//    }
//
//    @And("^the status code is 200$")
//    public void the_status_code_is_200() throws Throwable {
//    }
//
//    @And("^the response contains 10 urls in it$")
//    public void the_response_contains_10_urls_in_it() throws Throwable {
//
//    }
//
//
//}
