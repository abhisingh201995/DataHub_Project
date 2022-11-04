package com.Utilities;

import com.alm.apiobject.ALMException;
import com.alm.apiobject.AlmAPIObject;
import com.alm.apitest.BaseAlmAPITest;
import com.jayway.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class testMain {



    public static void main(String args[]) throws ALMException {

        BaseAlmAPITest baseAlmAPITest = new BaseAlmAPITest();
        baseAlmAPITest.loginToALM();

        AlmAPIObject almApiObject = new AlmAPIObject();
        Response response = almApiObject.getTestCaseStatusForGivenTestSetId("88883");

        ArrayList<HashMap> data = (response.getBody().path("entities"));

        ArrayList<HashMap> fields = (ArrayList<HashMap>) data.get(0).get("Fields");

        Response response1 = almApiObject.updateTestCaseStatus("1718284","Passed");



    }
}
