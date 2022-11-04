package com.Utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class SeleniumHelper extends SuiteInitializer{

    public void navigateToUrl(WebDriver driver,String url){
       driver.get(url);
    }

    public void clickElement(WebDriver driver, By element) {
        waitTime(1000);
        WebElement foundElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(element));

        foundElement.click();
        waitTime(1000);
    }

    public Boolean validateElementPresent(WebDriver driver, By element) {
        Boolean elementFound=false;
        waitTime();
        try {
            WebElement foundElement = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(element));
            elementFound=true;
        }catch (Exception e){
            elementFound=false;
        }

        return elementFound;
    }

    public Boolean validateElementPresentLong(WebDriver driver, By element) {
        Boolean elementFound=false;
        waitTime();
        try {
            WebElement foundElement = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.presenceOfElementLocated(element));
            elementFound=true;
        }catch (Exception e){
            elementFound=false;
        }

        return elementFound;
    }

    public void selectFromDropdown(WebDriver driver, By element, String valueToSelect){
        waitTime();
        Select list = new Select(driver.findElement(element));
        list.selectByVisibleText(valueToSelect);
    }

    public void selectFromDropdown(WebDriver driver, By element, Integer i){
        waitTime();
        Select list = new Select(driver.findElement(element));
        list.selectByIndex(i);
    }
    public void enterTextElement(WebDriver driver, By element, String text) {
        waitTime();

        WebElement foundElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(element));
        foundElement.clear();
        foundElement.sendKeys(text);
    }

    public String getElementText(WebDriver driver, By element){
        waitTime();
        return driver.findElement(element).getText();
    }

    public boolean isReadOnlyField(WebDriver driver, By element) {
        WebElement foundElement = null;
        try{
            foundElement = driver.findElement(element);
        }catch(Exception e){
        }
        Boolean readOnly = false;
        readOnly = ((foundElement.getAttribute("disabled") != null) || (foundElement.getAttribute("readonly") != null));
        return readOnly;
    }

    public List<String> getListOfElementsInDropDown(WebDriver driver, By element){
        waitTime();
        List<String> data = new ArrayList<>();
        Select s = new Select(driver.findElement(element));
        List <WebElement> op = s.getOptions();
        int size = op.size();
        for(int i =0; i<size ; i++){
            String options = op.get(i).getText();
            data.add(options);
        }
        return data;
    }

    public  void pressEnter(WebDriver driver,By element){
        driver.findElement(element).sendKeys(Keys.ENTER);
        waitTime();
    }

    public void scrollToBottomOfPage(WebDriver driver, By element){
        driver.manage().window().maximize();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    public void waitTime(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void waitTime(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
