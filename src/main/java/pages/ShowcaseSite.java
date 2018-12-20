package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShowcaseSite {

    private WebDriver driver;

    By uuidInputBox = By.cssSelector("input#uuid");
    By QARadioBttn = By.cssSelector("label[for='qaRadio'");
    By locateBttn = By.cssSelector("button#submitUUID");
    By stockImagesLinks = By.xpath("//td/strong[text()='vinSpecificStockImages']/ancestor::td/following-sibling::td[1]");
    By mediaTab = By.xpath("//a[text()='Media']");
    By stockImage = By.xpath("//h3[text()='VIN-Specific Stock Images']/following-sibling::img");
    By vinValue = By.xpath("//div[@id = 'tab_attributes']//table//td/strong[text()='vin']/parent::td/following-sibling::td[1]");


    public ShowcaseSite(WebDriver driver) {

        super();
        this.driver=driver;

    }

    public WebElement getUuidInputBox() {   return driver.findElement(uuidInputBox); }

    public WebElement getQARadioBttn() { return driver.findElement(QARadioBttn); }

    public WebElement getLocateBttn() { return driver.findElement(locateBttn); }

    //Getting the number of image links in the text
    public int getNumberOfStockImagesLinks(){
        String links = driver.findElement(stockImagesLinks).getText();
        System.out.println(links);
        int index = links.indexOf("https");
        int count = 0;
        while (index != -1) {
            System.out.println(count++);
            links = links.substring(index + 1);
            index = links.indexOf("https");
        }
        System.out.println("No of <links> in the input is : " + count);
        return count;
    }

    public WebElement getMediaTab(){return driver.findElement(mediaTab);}

    public int getNumberOfStockImages(){
        return driver.findElements(stockImage).size();
    }

    public WebElement getVinValue(){
        return driver.findElement(vinValue);
    }





}
