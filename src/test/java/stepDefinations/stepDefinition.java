package stepDefinations;


import Pages.RabbitMQSite;
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
import resources.Vehicle;
import resources.base;

import java.util.List;
import java.util.Random;
import java.time.*;

import static java.util.concurrent.TimeUnit.SECONDS;


public class stepDefinition extends base {
    public static Logger log = LogManager.getLogger(base.class.getName());

    //Object of RabbitMQ website
    private static RabbitMQSite rmq;
    private static String uuid = null;
    private static int testCount = 0;

    @Given("^Initialize the browser with chrome$")
    public void init_the_browser() throws Throwable {


        log.info(" ");
        log.info(" ");
        log.info(" ");
        log.info(" ");
        log.info("Initializing browser");
        this.driver = initializeDriver();

    }

    @Given("^Get a random (.+) vehicle with (.+) and Not removed$")
    public void get_a_random_bmw_vehicle_with_initial_and_removed(String oem, String status) throws Throwable {

        
        List<Vehicle> vehicles = q.getVehiclesByStatusAndRemovedAndOem(status, false, oem);

        System.out.println(vehicles.size()+" records found in the db matching the search 'oem'="+ oem+ ", 'status'=" + status);
        log.info(vehicles.size()+" records found in the db matching the search 'oem'="+ oem+ ", 'status'=" + status);

        Vehicle random = vehicles.get(new Random().nextInt(vehicles.size()));
        System.out.println(random);
        log.info("The uuid and vin of randomly chosen vehicle: "+random.getUuid() + ", " + random.getVin());

        v = new Vehicle(random);

    }

	@Given("^Navigate to \"([^\"]*)\"$")
	public void navigate_to(String rabbitMQLink) throws Throwable {
        

        driver.get(rabbitMQLink);
    }

	@When("^User enters username and password and logs in$")
	public void user_enters_username_and_password_and_logs_in() throws Throwable {
        

        rmq = new RabbitMQSite(driver);
        rmq.getUsernameBox().sendKeys(prop.getProperty("RMQuserName"));
        rmq.getPasswordBox().sendKeys(prop.getProperty("RMQpassword"));
        log.debug("logging to rabbitMQ account....");
        rmq.getLogin().submit();

    }

	@When("^Go to queues$")
	public void go_to_queues() throws Throwable {
        

        log.debug("Navigating to 'Queues' Tab....");
        rmq.getqueueTab().click();
	}

	@Given("^Set the next_scheduled timestamp to NULL for those who are not NULL$")
    public void set_the_nextscheduled_timestamp_to_null_for_those_who_are_not_null() throws Throwable {

       int response = q.updateVehicleSetNextSchedulesToNull();
       if(response<1)
           log.warn("No rows have been effected for the query to set null the next_schedule_on timestamp");

    }

    @Given("^Purge high and low priority queues$")
    public void purge_high_and_low_priority_queues() throws Throwable {
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

	@When("^Choose image-action-queue$")
	public void choose_image_action_queue() throws Throwable {
        

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

	@When("^generate a new uuid$")
	public void generate_a_new_uuid() throws Throwable {
        

        Utils utils = new Utils();
        this.uuid = utils.uuidGenerator();
        System.out.println(uuid);
        utils.UUIDStoreInExcel(uuid);
        log.info("Should successfully generated and stored the new uuid");

	}

	@When("^fill in the payload with the (.+) vin and new uuid$")
	public void fill_in_the_payload_with_the_vin_and_new_uuid(String oem) throws Throwable {
        

        String uuid = this.uuid;
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

        log.debug("feeling in the payload info..."+", uuid:"+ this.uuid);
        rmq.getPayload().sendKeys(payloadText);

	}

    @When("^fill in the payload with the (.+) vin and new uuid with force refresh$")
    public void fill_in_the_payload_with_the_vin_and_new_uuid_with_force_refresh(String oem) throws Throwable {
        

        String uuid = this.uuid;
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

	@When("^publish the message and wait for three seconds$")
	public void publish_the_message() throws Throwable {

        log.debug("publishing the message to the queue..."+", uuid:"+ this.uuid);
        rmq.getPublish().click();
    }

	@Then("^The new item should exist in the vehicle table and the status should be (.+)$")
	public void the_new_item_should_exist_in_the_vehicle_table_and_the_status_should_be_status(String status) throws Throwable {

        log.debug("Asserting if the new item exists in the MySQL 'vehicle' table"+", uuid:"+ this.uuid);
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        myVehicle = q.getVehicleByUUID(this.uuid);
        Assert.assertTrue(myVehicle!=null, "There is no Vehicle with the uuid "+ this.uuid);
        Assert.assertEquals((myVehicle.getStatus().toLowerCase()),status, "Status should be "+status+", but it's " + myVehicle.getStatus()+", uuid:"+ this.uuid );



	}

	@Then("^The next_scheduled_date of the new item should be less than (\\d+) hour$")
	public void the_next_scheduled_date_of_the_new_item_should_be_past_or_between_now_and_hour(int hours) throws Throwable {

        LocalDateTime currDate = LocalDateTime.now();
        LocalDateTime upperBoundDate = currDate.plusHours(hours);

        log.debug("Asserting if the new item's timestamp is less than an hour"+", uuid:"+ this.uuid);

//        Thread.sleep(2000);
        Assert.assertTrue(myVehicle.getNext_scheduled_on()!=null,"there is no next-Scheduled_on timestamp created"+", uuid:"+ this.uuid);
        Assert.assertTrue((myVehicle.getNext_scheduled_on().isBefore(upperBoundDate)), "the next_scheduled timestamp is not within "+hours +" hours"+", uuid:"+ this.uuid);


    }

	@Then("^Wait until the number of messages in the queue changes$")
	public void wait_until_the_number_of_messages_in_the_queue_changes() throws Throwable {

        int initialMessageCount = rmq.getReadyMessageCountInt();

//           Assert.assertEquals(initialMessageCount,0,"There are more than 0 messages in the queue ( which was purged)");

        int count = 0;
        int messageCount=initialMessageCount;

        //We wait until the count of messages in the queue changes until 2 mins
        log.info("Waiting until the number of masseges in the queue updates"+", uuid:"+ this.uuid);
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
        log.info("Waiting until the number of masseges in the queue updates"+", uuid:"+ this.uuid);
        while(messageCount == 0 && count < waitingTime)
        {
            messageCount = rmq.getReadyMessageCountInt();
            count++;
            if(count>100)
                driver.navigate().refresh();
            Thread.sleep(500);

        }

        Assert.assertTrue(count<waitingTime,"It didn't update the queue within "+count/2+" seconds interval");
        log.info("It took "+ count/2 + " secs to update the # of messages in the queue, and there are "+messageCount+" messages ");
    }

    @Then("^The next_scheduled_date of the new item should be between (\\d+)-(\\d+) days from now$")
    public void the_next_scheduled_date_of_the_new_item_should_be_between_days_from_now(int day1, int day2) throws Throwable {
        

        LocalDateTime myDate = myVehicle.getNext_scheduled_on();
        LocalDateTime currDate = LocalDateTime.now();

        log.debug("Asserting if the new item's timestamp is in between "+day1+" - "+day2 + " days"+", uuid:"+ this.uuid);
        log.info("The new timestamp date is "+ myDate + "==="+ myVehicle.getNext_scheduled_on());

        System.out.println("The new timestamp date is "+ myDate +"==="+myVehicle.getNext_scheduled_on());
//        Assert.assertTrue((myDate.isAfter(currDate.plusDays(day1)) && myDate.isBefore(currDate.plusDays(day2))),
//                "the next_scheduled_date of our vehicle is not within "+day1+" - "+day2+ " days."+", uuid:"+ this.uuid+"\n but it " +
//                        "is on "+ myVehicle.getNext_scheduled_on());
        Assert.assertTrue(myDate.isAfter(currDate.plusDays(day1)), "Is after error, should be "+currDate.plusDays(day1)+" should be before my date = "+ myDate);
        Assert.assertTrue(myDate.isBefore(currDate.plusDays(day2)),"is before error, should be"+currDate.plusDays(day2)+ " should be after than my date = "+ myDate);


    }

    @Then("^change the next-shchedule_date to the current date$")
    public void change_the_nex_shchedule_date_to_the_current_date() throws Throwable {
        

        int rowsEffected = q.updateVehicleNextScheduledOnToNow(uuid);
        log.debug("Asserting that only one raw changedupdated it's next_scheduled timestamp"+", uuid:"+ this.uuid);
        Assert.assertEquals(rowsEffected,1,"Only one row should be effected by this query"+", uuid:"+ this.uuid);

    }

    @And("^Query myVehicle again$")
    public void query_my_vehicle_again() throws Throwable{
        

        myVehicle =  q.getVehicleByUUID(uuid);
    }

    @Then ("^The next_scheduled_date of the new item should be more than (\\d+) years$")
    public void the_next_scheduled_date_of_the_new_vehicle_should_be_more_than_x_years(int years){
        

        LocalDateTime currDate = LocalDateTime.now();
        LocalDateTime upperBoundDate = currDate.plusYears(years);

        log.debug("Asserting that the next scheduled timestamp is more than " + years + " years late"+", uuid:"+ this.uuid);
        Assert.assertTrue(myVehicle.getNext_scheduled_on().isAfter(upperBoundDate),
                "the next_scheduled timestamp is not more than "+ years +" years"+", uuid:"+ this.uuid);

    }

    @Then("^The next_scheduled_date of the item should be greater but no more than (\\d+) hours$")
    public void the_next_scheduled_date_of_the_item_should_be_greater_but_no_more_than_hours(int hours) throws Throwable {
        

        LocalDateTime currDate = LocalDateTime.now();
        LocalDateTime upperBoundDate = currDate.plusHours(hours);

        log.debug("Asserting if the new item's timestamp is within now and an hour"+", uuid:"+ this.uuid);

        Assert.assertTrue((myVehicle.getNext_scheduled_on().isAfter(currDate) && myVehicle.getNext_scheduled_on().isBefore(upperBoundDate)),
                "the next_scheduled timestamp is more than "+ hours +" hours later, or less than current time"+", uuid:"+ this.uuid);
    }

    @And("^close the browser$")
    public void close_the_browser() throws Throwable {
        

        log.debug("closing the browser");
        driver.close();
    }

    @Then("^Vehicles with old and new uuids and should have have equal number of images mapped$")
    public void vehicles_with_old_and_new_uuids_and_should_have_equal_number_of_images_mapped() throws Throwable {
        log.debug("Comparing the amount of images of vehicles:"+v.getUuid() + " and " + this.uuid);
        int amount1 = q.getNumberOfImagesMappedToVehicleByUuid(v.getUuid());
        int amount2 = q.getNumberOfImagesMappedToVehicleByUuid(this.uuid);

        Assert.assertEquals(amount1,amount2, "The amount of images of these two vehicles are not equal, " +
                "uuids - amounts"+ v.getUuid() + " - " +amount1 + ":" + this.uuid + " - " + amount2);

        if(amount1!=amount2)
            log.error("The amount of images of these two vehicles are not equal, " +
                    "uuids - amounts"+ v.getUuid() + " - " +amount1 + ":" + this.uuid + " - " + amount2);



    }

    @Then("^set the removed flag to true for myVehicle$")
    public void set_the_removed_flag_to_true_for_myvehicle() throws Throwable {
        q.updateVehicleSetRemovedToTrue(uuid);
    }

    @And("^wait for (\\d+) seconds$")
    public void wait_for_3_seconds(int seconds) throws Throwable {
        Thread.sleep(seconds * 1000);

    }

    @And("^Purge the queue$")
    public void purge_the_queue() throws Throwable {
        log.debug("Clicking on purge button");
        rmq.getPurgeQueue().click();
    }

    @And("^close the alert tab and scroll up$")
    public void press_close_and_scroll_up() throws Throwable {
        rmq.getAlertCloseButton().click();

        WebElement element = driver.findElement(By.id("header"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }


}
