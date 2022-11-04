package com.Utilities;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigParser {

    public Map<String, String> inputParam = new HashMap<>();
    public Config config= ConfigFactory.parseResources("applicationSetting.conf");

    private ConfigParser(){

        Map<Object,Object> configuration = new HashMap<>();
        configuration=config.entrySet().stream().collect(Collectors.toMap(entry->entry.getKey(),entry->entry.getValue().unwrapped().toString()));

        configuration.forEach((k,v)->{
            inputParam.put(k.toString(),v.toString());
        });

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
