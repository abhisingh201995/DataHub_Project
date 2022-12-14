package com.Utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;

public class ResultSender extends SuiteInitializer {

    //todo update the kibana server address

    private static final ObjectMapper OM = new ObjectMapper();
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String ELASTICSEARCH_URL = "http://10.48.53.104:9200/app/suite";

    public static void send(final TestStatus testStatus){
        try {
            Unirest.post(ELASTICSEARCH_URL)
                    .header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                    .body(OM.writeValueAsString(testStatus)).asJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
