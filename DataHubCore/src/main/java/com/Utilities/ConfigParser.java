package com.Utilities;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.grpc.LoadBalancerRegistry;
import io.grpc.internal.PickFirstLoadBalancerProvider;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigParser {

    public Map<String, String> inputParam = new HashMap<>();
    public Config config= ConfigFactory.parseResources("applicationSetting.conf");

    private ConfigParser(){

        String projectId="repd-e-eng-adm-01";

        String qaSecret;

        LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, "qa_sensitive_key", "latest");
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            qaSecret = response.getPayload().getData().toStringUtf8();
            inputParam  = (JSONObject) new JSONParser().parse(qaSecret);

        } catch (Exception e) {
            Log.error(e.toString());
        }




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
