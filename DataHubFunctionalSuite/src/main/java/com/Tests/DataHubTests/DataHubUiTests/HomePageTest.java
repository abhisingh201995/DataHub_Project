package com.Tests.DataHubTests.DataHubUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhd.HomePage;
import com.WebPagesDhd.LoginPage;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;

@Test(groups = "HomePageTest")
public class HomePageTest extends BaseConfiguration {

    private static String title;
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeClass
    public void doLogin(){
        loginPage = new LoginPage(WebDriverFactory.getDriver());
        homePage=loginPage.doLoginUser();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of all hyperlink on home page")
    public void validateAllLinks(final JSONObject testData){
        DataLoader dataLoader = new DataLoader(testData);
        List<WebElement> links = homePage.getAllLinkButton();
        Iterator<WebElement> item = links.iterator();
        HttpURLConnection huc = null;
        int respCode = 200;

        String url;

        while (item.hasNext()){
            WebElement el= item.next();
            url = el.getAttribute("href");
            if(url == null || url.isEmpty()){
                Assert.assertTrue(false,el.getText()+"Link does not have a url:");
                continue;
            }else{
                Assert.assertTrue(true,el.getText()+": Link have valid url");
            }

        }
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of max PubSub message age")
    public void validateMaxPubSubMessageAge(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
       /* GlobalSettingPage globalSettingPage = homePage.clickOnGlobalSetting();
        globalSettingPage.validateMaxPubSubLimit(dataLoader.getTestParameter().get("MaxPubSubMsgAge"));
  */  }


    }
