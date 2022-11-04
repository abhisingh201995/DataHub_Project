package com.ukg.datahub.perf.utilities;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;

import java.util.HashMap;
import java.util.Map;

public class ConfigParser {

    public Map<String, String> inputParam = new HashMap<>();
    public Map<String, String> testDataPath = new HashMap<>();
    public Config config= ConfigFactory.parseResources("applicationSetting.conf");


    private ConfigParser() {

        inputParam.put("baseUrl", config.getString("conf.baseUrl"));
        inputParam.put("username", config.getString("conf.username"));
        inputParam.put("password", config.getString("conf.password"));
        inputParam.put("teamsWebHook", config.getString("conf.teamsWebHook"));
        inputParam.put("gcpBaseProject", config.getString("conf.gcpBaseProject"));
        inputParam.put("testResultBucket", config.getString("conf.testResultBucket"));
        inputParam.put("bitBucketUserName", config.getString("conf.bitBucketUserName"));
        inputParam.put("bitBucketToken", config.getString("conf.bitBucketToken"));

        inputParam.put("repd-e-eng-01", config.getString("serviceAccount.repd-e-eng-01"));
        inputParam.put("repd-d-eng-01", config.getString("serviceAccount.repd-d-eng-01"));

        inputParam.put("repd-e-eng-02", config.getString("serviceAccount.repd-e-eng-02"));
        inputParam.put("repd-d-eng-02", config.getString("serviceAccount.repd-d-eng-02"));

        inputParam.put("repd-e-eng-03", config.getString("serviceAccount.repd-e-eng-03"));
        inputParam.put("repd-e-eng-04", config.getString("serviceAccount.repd-e-eng-04"));
        inputParam.put("repd-e-eng-05", config.getString("serviceAccount.repd-e-eng-05"));
        inputParam.put("repd-e-eng-06", config.getString("serviceAccount.repd-e-eng-06"));
        inputParam.put("repd-e-eng-07", config.getString("serviceAccount.repd-e-eng-07"));
        inputParam.put("repd-e-eng-12", config.getString("serviceAccount.repd-e-eng-12"));
        inputParam.put("repd-e-eng-adm-01", config.getString("serviceAccount.repd-e-eng-adm-01"));
    }

    private static ConfigParser configParser;

    public static ConfigParser getConfigParser(){
        if(configParser ==null){
            configParser =new ConfigParser();
            return configParser;
        }
        else{
            return configParser;
        }
    }

}
