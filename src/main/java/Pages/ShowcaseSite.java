package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShowcaseSite {

    private WebDriver driver;

    By uuidInputBox = By.cssSelector("input#uuid");
    By QARadioBttn = By.cssSelector("input#qaRadio");
    By locateBttn = By.cssSelector("input##submitUUID");
    By stockImagesLinks = By.xpath("//td/strong[text()='vinSpecificStockImages']/ancestor::td/following-sibling::td[1]");

    public ShowcaseSite(WebDriver driver) {

        super();
        this.driver=driver;

    }

    public WebElement getUuidInputBox() {   return driver.findElement(uuidInputBox); }

    public WebElement getQARadioBttn() { return driver.findElement(QARadioBttn); }

    public WebElement getLocateBttn() { return driver.findElement(locateBttn); }

    public int getNumberOfStockImagesLinks(){
        String links = driver.findElement(stockImagesLinks).getText();
        int index = links.indexOf("https");
        int count = 0;
        while (index != -1) {
            count++;
            links = links.substring(index + 1);
            index = links.indexOf("is");
        }
        System.out.println("No of *links* in the input is : " + count);
        return count;
    }




}
