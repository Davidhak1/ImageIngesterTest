package Pages;

import org.apache.logging.log4j.core.appender.routing.PurgePolicy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;



    public class RabbitMQSite {
    static int count=0;

        public WebDriver driver;

        By usernameBox = By.cssSelector("input[name='username']");
        By passwordBox = By.cssSelector("input[name='password']");
        By loginBttn = By.cssSelector("input[type='submit']");

        By queuesTab =By.cssSelector("ul#tabs a[href*='#/queues']");
        By image_action_queue = By.xpath("//a[text()='image-action-queue']");
        By image_action_high_priority_queue = By.xpath("//a[text()='image-action-high-priority-queue']");
        By image_action_low_priority_queue = By.xpath("//a[text()='image-action-low-priority-queue']");

        By readyMessagesString = By.xpath("(//table[@class='facts facts-fixed-width']//div[@class='colour-key'])[1]/parent::td");

        By publishMessageLink = By.xpath("//h2[text()='Publish message']");
        By deliveryMode = By.cssSelector("select[name='delivery_mode']");
        By header1key = By.cssSelector("div#headers input#headers_1_mfkey");
        By header1value = By.cssSelector("div#headers input#headers_1_mfvalue");
        By header2key = By.cssSelector("div#headers input#headers_2_mfkey");
        By header2value = By.cssSelector("div#headers input#headers_2_mfvalue");

        By props1key = By.cssSelector("div#props input#props_1_mfkey");
        By props1value = By.cssSelector("div#props input#props_1_mfvalue");
        By props2key = By.cssSelector("div#props input#props_2_mfkey");
        By props2value = By.cssSelector("div#props input#props_2_mfvalue");

        By payload = By.cssSelector("textarea[name='payload']");
        By publish = By.cssSelector("input[value='Publish message'");
//        By delete_purge = By.cssSelector("h2:contains('Delete / purge')");
        By delete_purge = By.xpath("//h2[text()='Delete / purge']");

        By purgeQueue = By.cssSelector("input[value='Purge']");

        public RabbitMQSite(WebDriver driver) {

            super();
            this.driver=driver;

        }


        public WebElement getUsernameBox()
        {
            return driver.findElement(usernameBox);
        }
        public WebElement getPasswordBox()
        {
            return driver.findElement(passwordBox);
        }
        public WebElement getLogin(){return driver.findElement(loginBttn);}

        public WebElement getqueueTab()
        {
            return driver.findElement(queuesTab);
        }
        public WebElement getImageActionQueue()
        {
            return driver.findElement(image_action_queue);
        }
        public WebElement getImageActionHighQueue(){return driver.findElement(image_action_high_priority_queue);}
        public WebElement getImageActionLowQueue(){return driver.findElement(image_action_low_priority_queue);}

        public int getReadyMessageCountInt(){
//
            boolean flag=false;
            String str = null;

            while(flag!=true)
            {
                try{

                    WebElement el = driver.findElement(readyMessagesString);
                     str = el.getText();
                     flag=true;
                }catch (Exception e){

                }

            }

            int number = Integer.parseInt(str.replaceAll("\\D+",""));
            System.out.println(++count + ") Returning number: "+number);
            if(count%10==0)
            {
                System.out.println("-----------------------------------------------");
            }
            return number;
        }

        public WebElement getDeliveryMode()
        {
            return driver.findElement(deliveryMode);
        }
        public WebElement getPublishMessageLink() { return driver.findElement(publishMessageLink); }


        public WebElement getheader1key()
        {
            return driver.findElement(header1key);
        }
        public WebElement getheader1value()
        {
            return driver.findElement(header1value);
        }
        public WebElement getheader2key()
        {
            return driver.findElement(header2key);
        }
        public WebElement getheader2value()
        {
            return driver.findElement(header2value);
        }

        public WebElement getprops1key()
        {
            return driver.findElement(props1key);
        }
        public WebElement getprops1value()
        {
            return driver.findElement(props1value);
        }
        public WebElement getprops2key()
        {
            return driver.findElement(props2key);
        }
        public WebElement getprops2value()
        {
            return driver.findElement(props2value);
        }

        public WebElement getPayload(){return driver.findElement(payload);}
        public WebElement getPublish(){return driver.findElement(publish);}
        public WebElement getDeletePurgeLink(){return driver.findElement(delete_purge);}
        public WebElement getPurgeQueue(){return driver.findElement(purgeQueue);}






//        public int getPopUpSize()
//        {
//            return driver.findElements(popup).size();
//        }

    }
