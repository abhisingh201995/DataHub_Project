package com.WebPagesDhss;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;


/**
 * author: amit chauhan
 * date: 30 sep 2021
 * desc:baseClass for all pages, contaisn common functions. 
 * Your page will extend this.
 */


public class basePage {

	private By BUTTON_HamburgerMenu = By.xpath("//button[@automation-id='openCloseMenuHeaderButton']//span[@class='icon-k-menu']");
	private By signOutButton=By.xpath("//button[@title='Sign Out']");
	private By username = By.xpath("//input[@id='username']");
	private By password = By.xpath("//input[@id='password']");
	    
	    
	    
	 
	private By DANGER_ALERT = By.cssSelector(".msg-wrapper.alert.alert-danger");
	private By SUCCESS_ALERT = By.cssSelector(".msg-wrapper.alert.alert-success");
	String DangerText = "not_read";
	String SuccessText = "not_read";
	private SeleniumHelper seleniumHelper = new SeleniumHelper();
	
	public void waitForPageToLoad(WebDriver driver){
		
		String checkPageLoad = "checking";
		JavascriptExecutor jsexec = (JavascriptExecutor) driver;
		
		while(!(checkPageLoad.equals("complete")))
			{
				try {
					Thread.sleep(10);
					checkPageLoad=jsexec.executeScript("return document.readyState").toString();
			//		System.out.println("Page load status is....  "+checkPageLoad);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}
	
	
	public boolean ifAnyErrorSeen(WebDriver driver){
		
			DangerText = seleniumHelper.getElementText(driver, DANGER_ALERT);
			return seleniumHelper.isElementDisplayed(driver, DANGER_ALERT);
	}
	
	public boolean ifAnySuccessSeen(WebDriver driver){
			SuccessText = seleniumHelper.getElementText(driver, SUCCESS_ALERT);
			return seleniumHelper.isElementDisplayed(driver, DANGER_ALERT);
	}
	
	
	public String getDangerTextMessage(WebDriver driver){
			this.ifAnyErrorSeen(driver);// this call sets the var with message if seen any otherwise a default text
			return DangerText;
	}
	
	public String getSuccessTextMessage(WebDriver driver){
		this.ifAnySuccessSeen(driver);// this call sets the var with message if seen any otherwise a default text
		return SuccessText;
}
	
	protected boolean doLogout(WebDriver driver) {
		seleniumHelper.clickElement(driver, BUTTON_HamburgerMenu);
		seleniumHelper.clickElement(driver, signOutButton);
		this.waitForPageToLoad(driver);
		return seleniumHelper.isElementDisplayed(driver, username);
	}
	
}
