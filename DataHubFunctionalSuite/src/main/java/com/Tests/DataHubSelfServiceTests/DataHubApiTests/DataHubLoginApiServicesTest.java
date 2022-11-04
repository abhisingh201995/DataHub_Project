package com.Tests.DataHubSelfServiceTests.DataHubApiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.AccessTokenType;
import com.Utilities.DataLoader;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "DHD_Login_Api")
public class DataHubLoginApiServicesTest extends BaseConfiguration {

    @Test(dataProvider="userTestDataProvider")
    public void adminAuthTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);

        dataLoader.setPayload(dataLoader.getPayload()
                .replace("{username}", System.getenv("username"))
                .replace("{password}",System.getenv("password")));

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl()+dataLoader.getUrl(), dataLoader.getPayload(), null);
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Login failed for DataHub Core");
        dataPool.setAccessTokenDhd(response.path("token"));

        dataPool.setAccessTokenDhd(
                dataPool.getAllAccessToken()
                        .put(AccessTokenType.Dhd_Access_Token,response.path("token")));

    }

}
