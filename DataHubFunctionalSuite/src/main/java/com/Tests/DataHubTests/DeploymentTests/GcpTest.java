package com.Tests.DataHubTests.DeploymentTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.google.cloud.bigquery.Table;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GcpTest extends BaseConfiguration {

    @Test(dataProvider="userTestDataProvider", description = "Validation of storage bucket created with lifecycle of 10 Days")
    public void validateStageBucketLifeCycle(final JSONObject testData){
        DataLoader dataLoader = new DataLoader(testData);
        String lifeCycleRule = gcpHelper.getBucketDetails(System.getenv("targetGcpDataProject"),System.getenv("targetGcpDataProject")+"-stage")
                .get("LifeCycleRule").toString();
        Assert.assertEquals(lifeCycleRule,dataLoader.getExpectedResponse());

    }
    @Test(dataProvider="userTestDataProvider", description = "Validation of BigQuery expiration date set")
    public void validateBigQueryTableExpiration(final JSONObject testData){
        DataLoader dataLoader = new DataLoader(testData);
        List<String> listDataset= new ArrayList<>();
        String dataProject=System.getenv("targetGcpDataProject");
        listDataset=gcpHelper.listDatasets(dataProject);

        SoftAssert softAssertion = new SoftAssert();

        for(String dataset: listDataset){
            if(dataset.contains("_stage")){
                List<String> tables= new ArrayList<>();
                tables=gcpHelper.listTables(dataProject,dataset);

                tables.forEach(table->{
                    if(table.contains("cdc")){
                        Table tb = gcpHelper.getBigQueryTableDetails(dataProject,dataset,table);
                        Date date = new Date(tb.getCreationTime());
                        if(helper.isToday(date)){
                            System.out.println(table + " Expiration Date = "+tb.getExpirationTime());
                            softAssertion.assertNotNull(tb.getExpirationTime(), table+" Table does not contain expiration date");
                        }

                    }
                });
            }
        }
        softAssertion.assertAll();
    }

}
