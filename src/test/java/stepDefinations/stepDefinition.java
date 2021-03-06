package stepDefinations;


import pages.RabbitMQSite;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import resources.Utils;
import model.Vehicle;
import resources.base;

import java.util.List;
import java.util.Random;
import java.time.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;


public class stepDefinition extends base {
    private static Logger log = LogManager.getLogger(base.class.getName());

    //Object of RabbitMQ website
    private static RabbitMQSite rmq;
    private static String uuid = null;

    @Before
    public void before() {
//        long threadId = Thread.currentThread().getId();
//        String processName = ManagementFactory.getRuntimeMXBean().getName();
//        System.out.println("Started in thread: " + threadId + ", in JVM: " + processName);
//        System.out.println("---------------------------_______-----------__bEfOrE-----------------__________--------");

    }

    @Given("^Initialize the browser with chrome$")
    public void init_the_browser() throws Throwable {


        log.info(" ");
        log.info(" ");
        log.info(" ");
        log.info(" ");
        log.info("Initializing browser");
        this.driver = initializeDriver();

    }

	@Given("^Navigate to \"([^\"]*)\"$")
	public void navigate_to(String rabbitMQLink) {
        

        driver.get(rabbitMQLink);
    }

    @Given ("^Clean vehicle and vehicle_image tables from data created during prior tests$")
    public void clean_vehicle_and_vehicleimage_tables_from_data_created_during_prior_tests() throws Throwable {

        Utils u = new Utils();
        int response = u.getVehicleEligibleForRemovalAndSendToStore(35);

        if(response==1) {
            q.deleteImagesForVehiclesHavingUuidLength(35);
            q.deleteVehiclesHavingUuidLength(35);
        }
    }

    @Given("^Set the next_scheduled timestamp to NULL for those who are not NULL$")
    public void set_the_nextscheduled_timestamp_to_null_for_those_who_are_not_null()  {

        int response = q.updateVehicleSetNextSchedulesToNull();
        if(response<1)
            log.warn("No rows have been effected for the query to set null the next_schedule_on timestamp");

    }

    @Given("^Purge high and low priority queues$")
    public void purge_high_and_low_priority_queues()  {
        log.debug("Purging high and low priority queues");

        log.debug("Going to low-priority queue");
        rmq.getImageActionLowQueue().click();

        if(myVehicle==null)
            log.debug("Clicking on deletePurgeLink");
        rmq.getDeletePurgeLink().click();
        log.debug("Clicking on purge button");
        rmq.getPurgeQueue().click();
        log.debug("Navigating to Queues");
        rmq.getqueueTab().click();

        log.debug("Going to high-priority queue");
        rmq.getImageActionHighQueue().click();

        //2nd time we don't need to click on the hyperlink to purge the queue
        log.debug("Clicking on purge button");
        rmq.getPurgeQueue().click();
        log.debug("Navigating to Queues");
        rmq.getqueueTab().click();



        log.info("Successfully purged high and low priority queues");
        //        WebElement purge = rmq.getPurgeQueue();
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", purge);
        //        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", purge);

    }

    @When("^Get a random (.+) vehicle with (.+) and Not removed$")
    public void get_a_random_bmw_vehicle_with_initial_and_removed(String oem, String status)  {


        List<Vehicle> vehicles = q.getVehiclesByStatusAndRemovedAndOem(status, false, oem);

        System.out.println(vehicles.size()+" records found in the db matching the search 'oem'="+ oem+ ", 'status'=" + status);
        log.info(vehicles.size()+" records found in the db matching the search 'oem'="+ oem+ ", 'status'=" + status);
        Vehicle random =null;
        boolean repeat = true;

        while(repeat) {
            random = vehicles.get(new Random().nextInt(vehicles.size()));
            System.out.println("temp vehicle uuid = " + random.getUuid());
            repeat = q.getNumberOfVehiclesByVin(random.getVin()) > 1;   //returns true if there are more than 1 vehicle with the same vin
            System.out.println("_____________---------_________---------______-------_____----_____-----__---_-__--_--_-__---__-__-__---__-_--_--__-_---__-----_____----_____-------______---------_________---------_____________");

        }
        System.out.println("Random vehicle chosen: "+ random);
        log.info("The uuid and vin of randomly chosen vehicle: "+random.getUuid() + ", " + random.getVin());

        v = new Vehicle(random);

    }

    @When("^Get the vehicle with (.+) vin Not Removed$")
    public void get_the_vehicle_with(String vin)  {
        List<Vehicle> vehicles = q.getVehicleByVINAndNotRemoved(vin);
        Vehicle vehicle = vehicles.get(0);

        Assert.assertNotNull(vehicle, "There is no vehicle having vin - "+ vin+" ,and not removed");

        v = vehicle;
        System.out.println(v);
    }

    @When("^Extend the vehicle's original uuid$")
    public void extend_the_vehicles_uuid_to_a_new_one() throws Throwable {
        Utils u = new Utils();
        log.debug(String.format("extending audi uuid%s", v.getUuid()));
        uuid = u.audiUuidExtender(v.getUuid(),3);
        u.UUIDStoreInExcel(uuid);
        log.info(String .format("Successfully extended audi uuid: %s to uuid: %s",v.getUuid(), uuid));

    }

	@When("^User enters username and password and logs in$")
	public void user_enters_username_and_password_and_logs_in()  {
        

        rmq = new RabbitMQSite(driver);
        rmq.getUsernameBox().sendKeys(prop.getProperty("RMQuserName"));
        rmq.getPasswordBox().sendKeys(prop.getProperty("RMQpassword"));
        log.debug("logging to rabbitMQ account....");
        rmq.getLogin().submit();

    }

	@When("^Go to queues$")
	public void go_to_queues()  {
        

        log.debug("Navigating to 'Queues' Tab....");
        rmq.getqueueTab().click();
	}

	@When("^Choose image-action-queue$")
	public void choose_image_action_queue()  {
        

        log.debug("Going to 'image-action-queue'....");
        rmq.getImageActionQueue().click();
	}

    @When("^Choose image-action-high-priority queue$")
    public void choose_image_action_high_priority_queue() throws Throwable {
        

        log.debug("Going to 'image-action-high-priority queue'....");
        rmq.getImageActionHighQueue().click();
        Thread.sleep(500);

    }

    @When("^Choose image-action-low-priority queue$")
    public void choose_image_action_low_priority_queue() throws Throwable {
        

        log.debug("Going to 'image-action-low-priority queue'....");
        rmq.getImageActionLowQueue().click();
        Thread.sleep(500);
    }

	@When("^set the generic headers and properties for the publish$")
	public void set_the_bmw_generic_headers_and_properties_for_the_publish() throws Throwable {
        

        log.debug("Filling the headers and props to publish a message....");

        Thread.sleep(500);
        rmq.getPublishMessageLink().click();

        Select dropdown = new Select(rmq.getDeliveryMode());
        Thread.sleep(1000);
        dropdown.selectByValue("2");
        rmq.getheader1key().sendKeys(prop.getProperty("RMQ_headers1_key"));
        rmq.getheader1value().sendKeys(prop.getProperty("RMQ_headers1_value"));
        rmq.getheader2key().sendKeys(prop.getProperty("RMQ_headers2_key"));
        rmq.getheader2value().sendKeys(prop.getProperty("RMQ_headers2_value"));

        rmq.getprops1key().sendKeys(prop.getProperty("RMQ_props1_key"));
        rmq.getprops1value().sendKeys(prop.getProperty("RMQ_props1_value"));
        rmq.getprops2key().sendKeys(prop.getProperty("RMQ_props2_key"));
        rmq.getprops2value().sendKeys(prop.getProperty("RMQ_props2_value"));


    }

    @When("^set the removed flag to true for vehicles having uuid length greater than (\\d+)")
    public void set_the_removed_flag_to_true_for_vehicles_having_uuid_length_greater_than_35(int length)  {
        q.updateVehicleSetRemovedToTrueWhereLengthIs(length);
    }

    @When("^generate a new uuid$")
	public void generate_a_new_uuid() throws Throwable {
        

        Utils utils = new Utils();
        uuid = utils.uuidGenerator();
        System.out.println(uuid);
        utils.UUIDStoreInExcel(uuid);
        log.info("Should successfully generated and stored the new uuid");

	}

	@When("^fill in the payload with the (.+) vin and new uuid$")
	public void fill_in_the_payload_with_the_vin_and_new_uuid(String oem) {
        

        String uuid = stepDefinition.uuid;
        String vin = v.getVin();
        String accountId = v.getAccount_id();
        String oemText = "BMW_STOCK_IMAGES";
        if(oem.equals("aoa")){
            oemText = "AOA_STOCK_IMAGES";
        }


        String payloadText = "{\"imageProvider\":\""+oemText+"\",\"vin\":\""+vin+"\","+
                "\"productionNumber\":null,\"accountId\":\""+accountId+"\","+
                "\"uuid\":\""+uuid+"\",\"imageAction\":"+
                "\"DOWNLOAD_IMAGES_FOR_VEHICLE\",\"alwaysDownloadImages\":false,\"removedAt\":null}";

        log.debug("feeling in the payload info..."+", uuid:"+ stepDefinition.uuid);
        rmq.getPayload().sendKeys(payloadText);

	}

    @When("^fill in the payload with the (.+) vin and new uuid with force refresh$")
    public void fill_in_the_payload_with_the_vin_and_new_uuid_with_force_refresh(String oem)  {
        

        String uuid = stepDefinition.uuid;
        String vin = v.getVin();
        String accountId = v.getAccount_id();
        String oemText = "BMW_STOCK_IMAGES";
        if(oem.equals("aoa")){
            oemText = "AOA_STOCK_IMAGES";
        }

        String payloadText = "{\"imageProvider\":\""+oemText+"\",\"vin\":\""+vin+"\","+
                "\"productionNumber\":null,\"accountId\":\""+accountId+"\","+
                "\"uuid\":\""+uuid+"\",\"imageAction\":"+
                "\"DOWNLOAD_IMAGES_FOR_VEHICLE\",\"alwaysDownloadImages\":true,\"removedAt\":null}";

        log.debug("filling in the payload info...");
        rmq.getPayload().sendKeys(payloadText);

    }

	@When("^publish the message and wait for (\\d+) seconds$")
	public void publish_the_message(int sec) throws Throwable {

        log.debug("publishing the message to the queue..."+", uuid:"+ stepDefinition.uuid);
        rmq.getPublish().click();
        Thread.sleep(sec*1000);
    }

    @Then("^The new item should exist in the vehicle table$")
    public void the_new_item_should_exist_in_the_vehicle_table()  {
        log.debug("Asserting if the new item exists in the MySQL 'vehicle' table"+", uuid:"+ stepDefinition.uuid);

        myVehicle = q.getVehicleByUUID(stepDefinition.uuid);
        Assert.assertNotNull(myVehicle, "There is no Vehicle with the uuid "+ stepDefinition.uuid);
    }

	@Then("^The new item should exist in the vehicle table and the status should be (.+)$")
	public void the_new_item_should_exist_in_the_vehicle_table_and_the_status_should_be_status(String expectedStatus)  {

        log.debug("Asserting if the new item exists in the MySQL 'vehicle' table"+", uuid:"+ stepDefinition.uuid);

        myVehicle = q.getVehicleByUUID(stepDefinition.uuid);
        Assert.assertNotNull(myVehicle, "There is no Vehicle with the uuid "+ stepDefinition.uuid);
        Assert.assertEquals((myVehicle.getStatus().toLowerCase()),expectedStatus.toLowerCase(), "Status should be "+expectedStatus+"" +
                ", but it's " + myVehicle.getStatus()+", uuid:"+ stepDefinition.uuid );



	}

    @Then("^The status of the old vehicle should be (.+)$")
    public void the_status_of_old_vehicle_should_be(String expectedStatus)  {
        Assert.assertTrue(v.getStatus().equalsIgnoreCase(expectedStatus));
    }

	@Then("^The next_scheduled_date of the new item should be less than (\\d+) hour$")
	public void the_next_scheduled_date_of_the_new_item_should_be_past_or_between_now_and_hour(int hours)  {

        LocalDateTime currDate = LocalDateTime.now();
        LocalDateTime upperBoundDate = currDate.plusHours(hours);

        log.debug("Asserting if the new item's timestamp is less than an hour"+", uuid:"+ stepDefinition.uuid);

//        Thread.sleep(2000);
        Assert.assertNotNull(myVehicle.getNext_scheduled_on(),"there is no next-Scheduled_on timestamp created"+", uuid:"+ stepDefinition.uuid);
        Assert.assertTrue((myVehicle.getNext_scheduled_on().isBefore(upperBoundDate)), "the next_scheduled timestamp is not" +
                " within "+hours +" hours"+", uuid:"+ stepDefinition.uuid);

    }

	@Then("^Wait until the number of messages in the queue changes$")
	public void wait_until_the_number_of_messages_in_the_queue_changes() throws Throwable {

        int initialMessageCount = rmq.getReadyMessageCountInt();

        int count = 0;
        int messageCount=initialMessageCount;

        //We wait until the count of messages in the queue changes until 2 mins
        log.info("Waiting until the number of masseges in the queue updates"+", uuid:"+ stepDefinition.uuid);
        while(messageCount == initialMessageCount && count < 360)
        {
            messageCount = rmq.getReadyMessageCountInt();
            count++;
            Thread.sleep(500);

        }

        Assert.assertTrue(count<360,"It didn't update the queue within "+count/2+"interval");
        log.info("It took "+ count/2 + " secs to update the # of messages in the queue, and there are "+messageCount+" messages ");

	}

    @And("^Wait until the number of messages in the queue is 1$")
    public void wait_until_the_number_of_messages_in_the_queue_is_1() throws Throwable {
        int messageCount = rmq.getReadyMessageCountInt();
        int waitingTime = 360;
        int count = 0;

        //We wait until the count of messages in the queue is 1 until 2 mins, 3mins
        log.info("Waiting until the number of masseges in the queue updates"+", uuid:"+ stepDefinition.uuid);
        while(messageCount == 0 && count < waitingTime)
        {
            messageCount = rmq.getReadyMessageCountInt();
            count++;
            if(count%100==0)
                driver.navigate().refresh();
            Thread.sleep(500);

        }

        Assert.assertTrue(count<waitingTime,"It didn't update the queue within "+count/2+" seconds interval");
        log.info("It took "+ count/2 + " secs to update the # of messages in the queue, and there are "+messageCount+" messages ");
    }

    @Then("^The next_scheduled_date of the new item should be between (\\d+)-(\\d+) days from now$")
    public void the_next_scheduled_date_of_the_new_item_should_be_between_days_from_now(int day1, int day2)  {
        

        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime currDate = LocalDateTime.now();

        log.debug("Asserting if the new item's timestamp is in between "+day1+" - "+day2 + " days"+", uuid:"+ stepDefinition.uuid);
        log.info("The new timestamp date is "+ myDate + "==="+ myVehicle.getNext_scheduled_on());

        System.out.println("The new timestamp date is "+ myDate +"==="+myVehicle.getNext_scheduled_on());
//        Assert.assertTrue((myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2))),
//                "the next_scheduled_date of our vehicle is not within "+day1+" - "+day2+ " days."+", uuid:"+ this.uuid+"\n but it " +
//                        "is on "+ myVehicle.getNext_scheduled_on());
        Assert.assertTrue(myDate.isAfter(currDate.plusDays(day1)), "Is after error, should be "+currDate.plusDays(day1)+" should be before my date = "+ myDate);
        Assert.assertTrue(myDate.isBefore(currDate.plusDays(day2)),"is before error, should be"+currDate.plusDays(day2)+ " should be after than my date = "+ myDate);


    }

    @Then("^The next_scheduled_date of the new item should be between (\\d+)-(\\d+) days or more then (\\d+) years from now$")
    public void the_next_scheduled_date_of_the_new_item_should_be_between_days_from_now_or_more_then_years(int day1, int day2, int year)  {


        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime currDate = LocalDateTime.now();

        log.debug("Asserting if the new item's timestamp is in between "+day1+" - "+day2 + " days or "+year+" years later, uuid:"+ stepDefinition.uuid);

//   BELOW COMMENTS ARE FOR TROUBLESHOOTING

//        log.info("The new timestamp date is "+ myDate + "==="+ myVehicle.getNext_scheduled_on());
//        System.out.println("The new timestamp date is "+ myDate +"==="+myVehicle.getNext_scheduled_on());

        Assert.assertTrue(((myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2))||
                        myDate.isAfter(currDate.plusYears(year)))),
                "the next_scheduled_date of our vehicle is not within "+day1+" - "+day2+ " days. or after " + year +
                        " years, uuid:"+ stepDefinition.uuid+"\n but it " +
                        "is on "+ myVehicle.getNext_scheduled_on());

    }

    @Then("^change the next-shchedule_date to the current date$")
    public void change_the_nex_shchedule_date_to_the_current_date()  {
        

        int rowsEffected = q.updateVehicleNextScheduledOnToNow(uuid);
        log.debug("Asserting that only one raw changedupdated it's next_scheduled timestamp"+", uuid:"+ stepDefinition.uuid);
        Assert.assertEquals(rowsEffected,1,"Only one row should be effected by this query"+", uuid:"+ stepDefinition.uuid);

    }

    @And("^Query myVehicle again$")
    public void query_my_vehicle_again() {
        

        myVehicle =  q.getVehicleByUUID(uuid);
    }

    @Then ("^The next_scheduled_date of the new item should be the same as for the original one$")
    public void comparing_next_scheduled_times(){

        log.debug("Asserting that the next_scheduled_on fields for original and new items are equal");
        Assert.assertEquals(myVehicle.getNext_scheduled_on(),v.getNext_scheduled_on(),
                "the next_scheduled_on fields for original and new items are not equal - mine: " + myVehicle.getNext_scheduled_on() +
                " : original: " + v.getNext_scheduled_on());

    }

    @Then("^The next_scheduled_date of the item should be greater than now but no more than (\\d+) hours$")
    public void the_next_scheduled_date_of_the_item_should_be_greater_but_no_more_than_hours(int hours)  {
        

        LocalDateTime currDate = LocalDateTime.now();
        LocalDateTime upperBoundDate = currDate.plusHours(hours);

        log.debug("Asserting if the new item's timestamp is within now and an hour"+", uuid:"+ stepDefinition.uuid);

        Assert.assertTrue((myVehicle.getNext_scheduled_on().isAfter(currDate) && myVehicle.getNext_scheduled_on().isBefore(upperBoundDate)),
                "the next_scheduled timestamp is more than "+ hours +" hours later, or less than current time "+", uuid:  "+ stepDefinition.uuid);
    }

    @Then("^close the browser$")
    public void close_the_browser()  {
        

        log.debug("closing the browser");
        driver.close();
    }

    @Then("^Vehicles with old and new uuids and should have have equal number of images mapped$")
    public void vehicles_with_old_and_new_uuids_and_should_have_equal_number_of_images_mapped()  {
        log.debug("Comparing the amount of images of vehicles:"+v.getUuid() + " and " + stepDefinition.uuid);
        int amount1 = q.getNumberOfImagesMappedToVehicleByUuidNotRemoved(v.getUuid());
        int amount2 = q.getNumberOfImagesMappedToVehicleByUuidNotRemoved(stepDefinition.uuid);

        Assert.assertEquals(amount1,amount2, "The amount of images of these two vehicles are not equal, " +
                "uuids - "+ v.getUuid() + " - " +amount1 + " : " + stepDefinition.uuid + " - " + amount2);

        if(amount1!=amount2)
            log.error("The amount of images of these two vehicles are not equal, " +
                    "uuids - "+ v.getUuid() + " - " +amount1 + " : " + stepDefinition.uuid + " - " + amount2);



    }

    @Then("^set the removed flag to true for myVehicle$")
    public void set_the_removed_flag_to_true_for_myvehicle() {
        q.updateVehicleSetRemovedToTrue(uuid);
    }

    @And("^Query myVehicle until the date_time is greater than now$")
    public void query_myvehicle_until_the_datetime_is_greater_than_now()  {


    }

    @And("^Query myVehicle until the status is None, Partial or Complete$")
    public void query_myvehicle_until_the_status_is_none_partial_or_complete() throws Throwable {

        String status = null;
        boolean flag = false;
        int count = 0;

        while(!flag && count++<60)
        {
            Thread.sleep(2000);
            if(count<10) {
                System.out.println(count + " -- " + status);
            }else{
                System.out.println(count + " - " + status);
            }
            log.debug(String.format("Waiting until the new item's status is nonr, partial or complete --  uuid:%s", stepDefinition.uuid));
            myVehicle = q.getVehicleByUUID(stepDefinition.uuid);
            status = myVehicle.getStatus();
            flag =  (status.equalsIgnoreCase("NONE") || status.equalsIgnoreCase("PARTIAL") || status.equalsIgnoreCase("COMPLETE"));

        }

        System.out.println(++count + " - " + status);
    }

    @And("^Query myVehicle until the date_time is between (\\d+)-(\\d+) days from now$")
    public void query_myvehicle_until_the_datetime_is_between_68_days_from_now(int day1, int day2) throws Throwable {

        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime currDate = LocalDateTime.now();
        boolean flag = false;
        int count = 0;

        while(!flag && count++<40)
        {
            Thread.sleep(2000);
            if(count<10) {
                System.out.println(count + " -- " + myDate);
            }else{
                System.out.println(count + " - " + myDate);

            }
            log.debug("Waiting until the new item's timestamp is between "+day1+" - "+day2 + " days"+", uuid:"+ stepDefinition.uuid);
            myVehicle = q.getVehicleByUUID(stepDefinition.uuid);
            myDate =  myVehicle.getNext_scheduled_on();
            flag = (myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2)));

        }
        System.out.println(count + " -- " + myDate);

        Assert.assertTrue(myDate.isAfter(currDate.plusDays(day1)), "Is after error, should be "+currDate.plusDays(day1)+" should be before my date = "+ myDate);
        Assert.assertTrue(myDate.isBefore(currDate.plusDays(day2)),"is before error, should be"+currDate.plusDays(day2)+ " should be after than my date = "+ myDate);

    }

    @And("^Query myVehicle until the date_time is between (\\d+)-(\\d+) days or more then (\\d+) years from now$")
    public void query_myvehicle_until_the_datetime_is_between_6_8_days_or_more_then_60000_years_from_now(int day1, int day2, int year) throws Throwable {

        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime currDate = LocalDateTime.now();
        boolean flag = false;
        int count = 0;

        while(!flag && count++<40)
        {
            Thread.sleep(2000);
            if(count<10) {
                System.out.println(count + " -- " + myDate);
            }else{
                System.out.println(count + " - " + myDate);
            }
            log.debug(String.format("Waiting until the new item's timestamp is between %d - %d days, or greatert %d years --  uuid:%s", day1, day2, year, stepDefinition.uuid));
            myVehicle = q.getVehicleByUUID(stepDefinition.uuid);
            myDate =  myVehicle.getNext_scheduled_on();
            flag = ((myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2)) ||
                    myDate.isAfter(currDate.plusYears(year))));

        }
        System.out.println(++count + " - " + myDate);


    }

    @Then("^Query myVehicle until the date_time is between (\\d+)-(\\d+) days or equals to the original's next_scheduled date$")
    public void query_myvehicle_until_the_datetime_is_between_68_days_or_equals_to_the_originals_nextscheduled_date(int day1, int day2) throws Throwable {
        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime currDate = LocalDateTime.now();
        LocalDateTime originalDate = v.getNext_scheduled_on();
        boolean flag = false;
        int count = 0;

        while(!flag && count++<40)
        {
            Thread.sleep(2000);
            if(count<10) {
                System.out.println(count + " -- " + myDate);
            }else{
                System.out.println(count + " - " + myDate);

            }
            log.debug("Waiting until the new item's timestamp is between "+day1+" - "+day2 + " days or equals to the original's"+", uuid:"+ stepDefinition.uuid);
            myVehicle = q.getVehicleByUUID(stepDefinition.uuid);
            myDate =  myVehicle.getNext_scheduled_on();
            flag = ((myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2)) ||
                    myDate==originalDate));

        }
        System.out.println(++count + " - " + myDate);
    }

    @Then("^The next_scheduled_date of the new item should be between (\\d+)-(\\d+) days or equals to the original's next_scheduled date$")
    public void the_nextscheduleddate_of_the_new_item_should_be_between_68_days_or_equals_to_the_originals_nextscheduled_date(int day1, int day2)  {
        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime currDate = LocalDateTime.now();
        LocalDateTime originalDate = v.getNext_scheduled_on();
        log.debug("Asserting if the new item's timestamp is in between "+day1+" - "+day2 + " days or equals to the original's --- uuid:"+ stepDefinition.uuid);

        Assert.assertTrue(((myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2))||
                        myDate == originalDate)),
                String.format("the next_scheduled_date of our vehicle is not within %d - %d days. or not equal to original's date --- uuid:%s\n" +
                        " but it is on %s. Original's date = %s", day1, day2, stepDefinition.uuid, myVehicle.getNext_scheduled_on(), v.getNext_scheduled_on()));

    }

    @And("^Query myVehicle until the date_time is between (\\d+)-(\\d+) days or equal to last_modified_on timestamp$")
    public void query_myvehicle_until_the_datetime_is_between_68_days_or_equal_to_lastmodifiedon_timestamp(int day1,int day2) throws Throwable {
        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime currDate = LocalDateTime.now();
        boolean flag = false;
        int count = 0;

        while(!flag && count++<40)
        {
            Thread.sleep(2000);
            if(count<10) {
                System.out.println(count + " -- " + myDate);
            }else{
                System.out.println(count + " - " + myDate);

            }
            log.debug("Waiting until the new item's timestamp is between "+day1+" - "+day2 + " days or equals to the last_modified_on date"+", uuid:"+ stepDefinition.uuid);
            myVehicle = q.getVehicleByUUID(stepDefinition.uuid);
            myDate =  myVehicle.getNext_scheduled_on();
            flag = ((myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2)) ||
                    myDate.isBefore(currDate.plusMinutes(5))));

        }
        System.out.println(++count + " - " + myDate);
    }

    @And("^The next_scheduled_date of the new item should be between (\\d+)-(\\d+) days or equal to last_modified_on timestamp$")
    public void the_nextscheduleddate_of_the_new_item_should_be_between_68_days_or_equal_to_lastmodifiedon_timestamp(int day1,int day2)  {
        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime lastUpdate = myVehicle.getLast_modified_on();
        LocalDateTime currDate = LocalDateTime.now();
        log.debug("Asserting if the new item's timestamp is in between "+day1+" - "+day2 + " days or equals to last_updateed_on date --- uuid:"+ uuid);

        Assert.assertTrue(((myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2))))||
                        myDate.isEqual(lastUpdate),
                String.format("the next_scheduled_date of our vehicle is not within %d - %d days, and it's not equal to last_updated_on date (%s) --- uuid:%s\n" +
                        " but it is on %s.", day1, day2, lastUpdate, uuid, myVehicle.getNext_scheduled_on(), uuid));    }

    @And("^The next_scheduled_date of the new item should be the same as last_updated date and less than (\\d+) mins$")
    public void the_nextscheduleddate_of_the_new_item_should_be_the_same_as_lastupdated_date_and_less_than_5_mins(int minutes) throws Throwable {

        LocalDateTime currTime = LocalDateTime.now();
        log.debug("Asserting that the next_scheduled_on is the same as last_modified and less than 5 mins");
        Assert.assertEquals(myVehicle.getNext_scheduled_on(),myVehicle.getLast_modified_on(),
                "the next_scheduled_on isn't equal last_modified. next_scheduled: " + myVehicle.getNext_scheduled_on() +
                        " : last_modified: " + myVehicle.getLast_modified_on());
        Assert.assertTrue(myVehicle.getNext_scheduled_on().isBefore(currTime.plusMinutes(minutes)), "new item's next_schedule date is not within 5" +
                " minutes from now next_scheduled_on : " + myVehicle.getNext_scheduled_on() + "current time: " + currTime);
    }

    @And("^wait for (\\d+) seconds$")
    public void wait_for_3_seconds(int seconds) throws Throwable {
        Thread.sleep(seconds * 1000);

    }

    @And("^Purge the queue$")
    public void purge_the_queue()  {
        log.debug("Clicking on purge button");
        rmq.getPurgeQueue().click();
    }

    @And("^close the alert tab and scroll up$")
    public void press_close_and_scroll_up()  {
        rmq.getAlertCloseButton().click();

        WebElement element = driver.findElement(By.id("header"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }


}
