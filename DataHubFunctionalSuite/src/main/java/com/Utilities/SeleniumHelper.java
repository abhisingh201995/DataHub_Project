package com.Utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class SeleniumHelper {

	public void navigateToUrl(WebDriver driver,String url){
		driver.manage().window().maximize();
		driver.get(url);
	}

	public void clickElement(WebDriver driver, By element) {
		waitTime(1000);
		WebElement foundElement = (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.elementToBeClickable(element));

		foundElement.click();
		waitTime(1000);
	}
	
	
	public  void clearText(WebDriver driver,By locator) {
		
//		driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
		driver.findElement(locator).clear();
		
	}
	
public  void clearTextBackSpace(WebDriver driver,By locator) {
		
		WebElement el = driver.findElement(locator);
		el.sendKeys(Keys.CONTROL+ "a");el.sendKeys(Keys.BACK_SPACE);
		
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

	/**
	 * author: amit chauhan
	 * date: 30 sep 2021
	 * desc: verify if element is displayed and has some text, catch exception, return actual text or a const string
	 */
	public String getElementText(WebDriver driver, By element) {
		// we need to use Try Catch here. There may be situation when we are unable to
		// findElement, we dont want code throw exception:achauhan
		try {
			// till expetcetd
			WebElement foundElement = (new WebDriverWait(driver, 10))
					.until(ExpectedConditions.visibilityOfElementLocated(element));
			return foundElement.getText();
		} catch (Exception e) {
			return "element_not_found";
		}
	}

	/**
	 * author: amit chauhan
	 * date: 30 sep 2021
	 * desc: verify if elemnt is displayed, catch exception, return true or false
	 */
	public boolean isElementDisplayed(WebDriver driver, By element) {
		try {
			return driver.findElement(element).isDisplayed();
		} catch (Exception e) {
			return false;// if element not found
		}
	}

	public String getElementAttribute(WebDriver driver, By element){
	    try {
		     return driver.findElement(element).getAttribute("value");
	    }catch (Exception e) {
		     return "element_not_found";
	    }
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

	public void refreshAndLoadPage(WebDriver driver){
		driver.manage().window().maximize();
		driver.navigate().refresh();
		waitTime(5000);
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
