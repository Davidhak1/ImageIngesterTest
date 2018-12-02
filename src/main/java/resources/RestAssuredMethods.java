package resources;

import cucumber.api.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.codec.binary.Base64;
import resources.base;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;


public class RestAssuredMethods extends base {

    public void restassure() throws Throwable {
        RestAssured.baseURI = prop.getProperty("SULZER_URI");

        given().
                header("user", "user").
                header("password", "password").
                when().
                get("/WBAJA5C5XKG900336?pov=driverdoor,dashboard&angle=45,90,135,180,225,270,315,360&bkgnd=transparent&imgtype=png&height=960&width=1280").
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().body("imageUrls.size()", is(10));
    }
}