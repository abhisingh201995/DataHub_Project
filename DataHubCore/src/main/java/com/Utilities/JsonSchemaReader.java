package com.Utilities;

import org.testng.reporters.Files;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JsonSchemaReader extends SuiteInitializer {

    public JsonSchemaReader(String schemaPath) {

        Map<String,String> testDataPath= new HashMap<>();
        File folder= new File(schemaPath);

        try {

            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile()) {
                    testDataPath.put(fileEntry.getName(), fileEntry.getCanonicalPath());
                }
            }
        }catch (Exception e)
        {
            System.out.println("unable to read file");
        }

        HashMap<String, String> testDataPool = new HashMap<String, String >();
        testDataPath.forEach((k,v)->{
            try{
                File file = new File(v);
                String content = (String) Files.readFile(file);
                testDataPool.put(k, content);

            }catch (Exception e) {throw new Error("Unable to read files"); }
        });

        dataPool.setJsonSchemaPool(testDataPool);
    }
}
