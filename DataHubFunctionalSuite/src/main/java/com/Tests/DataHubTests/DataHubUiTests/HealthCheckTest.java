package com.Tests.DataHubTests.DataHubUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.BitBucketConnectHelper;
import com.Utilities.DataLoader;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Test(groups = "HealthCheckTest")
public class HealthCheckTest extends BaseConfiguration {

    BitBucketConnectHelper connectHelper = new BitBucketConnectHelper();

    @Test(dataProvider = "userTestDataProvider", description = "Validation of JSON Schema files for reference table in SQL")
    public void validateSchemaFilesQuery(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        List<String> files = connectHelper.getFilesFromFolder(dataLoader.getTestParameter().get("Schema"));
        SoftAssert softAssertion = new SoftAssert();
        Pattern p = Pattern.compile(".*\\` *(.*) *\\`.*");
        for (String item : files) {
            String data = connectHelper.getFileData(item, dataLoader.getTestParameter().get("Schema"));
            Matcher m = p.matcher(data);
            int count=1;
            while(m.find()){
                    String text = m.group(count);
                    System.out.println(text);
                    softAssertion.assertTrue(text.contains("#project#"), "Project validation failed for file:" + item+" Sql reference: "+text);
                    softAssertion.assertTrue(text.contains("schema"), "Table schema validation failed for file:" + item+" Sql reference: "+text);
                    count++;
            }
        }
        softAssertion.assertAll();
    }

    @Test(dataProvider = "userTestDataProvider", description = "Publishing vulnerability scan results")
    public void validateVulnerabilityScan(final JSONObject testData) throws IOException {
        DataLoader dataLoader = new DataLoader(testData);
        gcpHelper.getVulnerabilityScanReport("repd-e-eng-01",
                "https://gcr.io/repd-e-eng-01/bdpmgr_7.0.3");

    }



}
