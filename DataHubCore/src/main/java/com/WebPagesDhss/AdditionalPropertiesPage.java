package com.WebPagesDhss;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdditionalPropertiesPage {
    private WebDriver driver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    private By TEXTFIELD_PROJECTID= By.id("projectId");
    private By TEXTFIELD_GCPPROJECTNAME= By.id("gcpProjectDesc");
    private By BUTTON_ADDANDDOWNLOAD= By.cssSelector("button[title='Add and Download Key']");
    private By BUTTON_DELETE= By.id("//span[@class='icon-k-delete edap-btn-icon']");

    public AdditionalPropertiesPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean getProjectIdField(){
        return seleniumHelper.isReadOnlyField(driver,TEXTFIELD_PROJECTID );
    }

    public boolean getGCPProjectNameField(){
        return seleniumHelper.isReadOnlyField(driver,TEXTFIELD_GCPPROJECTNAME );
    }

    public void clickAddAndDownloadButton(){ seleniumHelper.clickElement(driver,BUTTON_ADDANDDOWNLOAD);
    }

    public void clickDeleteButton(){ seleniumHelper.clickElement(driver,BUTTON_DELETE);
    }
}
