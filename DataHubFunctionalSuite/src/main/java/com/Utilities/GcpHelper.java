package com.Utilities;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.paging.Page;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import com.google.cloud.devtools.containeranalysis.v1.ContainerAnalysisClient;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.storage.*;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import io.grafeas.v1.GrafeasClient;
import io.grafeas.v1.Occurrence;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GcpHelper {

    BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
    ConfigParser configParser = ConfigParser.getConfigParser();

    public List<HashMap<String, Object>> queryDataSet(String sql, List<String> columns){
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(sql)
                        .setUseLegacySql(false)
                        .build();
        // job ID so that we can safely retry.
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        // Wait for the query to complete.
        try {
            queryJob = queryJob.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check for errors
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }

        // Get the results.
        TableResult result = null;
        try {
            result = queryJob.getQueryResults();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<HashMap<String, Object>> outputData= new ArrayList<>();

        // Print all pages of the results.
        for (FieldValueList row : result.iterateAll()) {

            columns.forEach(item->{
                HashMap<String,Object> data = new HashMap<>();
                data.put(item,row.get(item).getRecordValue().get(0).getStringValue());
                outputData.add(data);
            });


        }

        return outputData;


    }

    public void uploadObject(
            String projectId, String bucketName, String objectName, String filePath){

        Storage storage=null;
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(configParser.inputParam.get("serviceAccount."+projectId)));
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }catch (Exception e){
            System.out.println("Unable to get authentication json");
        }
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try {
            storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        }catch (IOException e){
            System.out.println("unable to read file");
        }

        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    /**
     * Publishing data to PubSub topic
     * @param projectId
     * @param topicId
     * @throws IOException
     * @throws InterruptedException
     */
    public static void publishToPubSub(String projectId, String topicId, List<String> messages)
            throws IOException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

           // List<String> messages = Arrays.asList("first message", "second message");


            JSONObject data1=null;

            //jsonstring

            for (final String message : messages) {
                ByteString data = ByteString.copyFromUtf8(data1.toJSONString());
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> future = publisher.publish(pubsubMessage);

                // Add an asynchronous callback to handle success / failure
                ApiFutures.addCallback(
                        future,
                        new ApiFutureCallback<String>() {

                            @Override
                            public void onFailure(Throwable throwable) {
                                if (throwable instanceof ApiException) {
                                    ApiException apiException = ((ApiException) throwable);
                                    // details on the API exception
                                    System.out.println(apiException.getStatusCode().getCode());
                                    System.out.println(apiException.isRetryable());
                                }
                                System.out.println("Error publishing message : " + message);
                            }

                            @Override
                            public void onSuccess(String messageId) {
                                // Once published, returns server-assigned message ids (unique within the topic)
                                System.out.println("Published message ID: " + messageId);
                            }
                        },
                        MoreExecutors.directExecutor());
            }
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

    public void getVulnerabilityScanReport( String projectId, String resourceUrl) throws IOException {

        resourceUrl="https://gcr.io/repd-e-eng-01/bdpmgr_7.0.3@sha256:2c070c982f6b569625bb7b643d72f55da716f3dc1ec3420a7a2f4042bf65796a";
        projectId="repd-e-eng-01";


        final String projectName = ProjectName.format(projectId);
        final String filterStr = String.format("resourceUrl=\"%s\"", resourceUrl);

        GrafeasClient client = ContainerAnalysisClient.create().getGrafeasClient();

        int i = 0;
        for (Occurrence o : client.listOccurrences(projectName, filterStr).iterateAll()) {
            // Write custom code to process each Occurrence here
            System.out.println(o.getName());
            i++;
        }

        System.out.println("Total vulnerability = " + i);

    }

    public List<String> listBucketObjects(String projectId, String bucketName) {

        Storage storage=null;
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(configParser.inputParam.get("serviceAccount."+projectId)));
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }catch (Exception e){
            System.out.println("Unable to get authentication json");
        }
        Page<Blob> blobs = storage.list(bucketName);

        List<String> files = new ArrayList<>();
        for (Blob blob : blobs.iterateAll()) {
            System.out.println(blob.getName());
            files.add(blob.getName());
        }

        return files;
    }

    public String readFileFromBucket(String projectId, String bucketName, String fileName){
        Storage storage=null;
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(configParser.inputParam.get("serviceAccount."+projectId)));
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }catch (Exception e){
            System.out.println("Unable to get authentication json");
        }
        Blob blob = storage.get(bucketName, fileName);
        String fileContent = new String(blob.getContent());
        return fileContent;
    }

    public HashMap<String, Object> getBucketDetails(String projectId, String bucketName)  {

        Storage storage=null;
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(configParser.inputParam.get("serviceAccount."+projectId)));
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }catch (Exception e){
            System.out.println("Unable to get authentication json");
        }

        Bucket bucket =
                storage.get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.values()));

        HashMap<String,Object> bucketMetaData= new HashMap<>();
        bucketMetaData.put("BucketName" , bucket.getName());
        bucketMetaData.put("DefaultEventBasedHold" , bucket.getDefaultEventBasedHold());
        bucketMetaData.put("DefaultKmsKeyName" , bucket.getDefaultKmsKeyName());
        bucketMetaData.put("Id" , bucket.getGeneratedId());
        bucketMetaData.put("IndexPage" , bucket.getIndexPage());
        bucketMetaData.put("Location" , bucket.getLocation());
        bucketMetaData.put("LocationType" , bucket.getLocationType());
        bucketMetaData.put("Metageneration" , bucket.getMetageneration());
        bucketMetaData.put("NotFoundPage" , bucket.getNotFoundPage());
        bucketMetaData.put("RetentionEffectiveTime" , bucket.getRetentionEffectiveTime());
        bucketMetaData.put("RetentionPeriod" , bucket.getRetentionPeriod());
        bucketMetaData.put("RetentionPolicyIsLocked" , bucket.retentionPolicyIsLocked());
        bucketMetaData.put("RequesterPays" , bucket.requesterPays());
        bucketMetaData.put("SelfLink" , bucket.getSelfLink());
        bucketMetaData.put("StorageClass" , bucket.getStorageClass().name());
        bucketMetaData.put("TimeCreated" , bucket.getCreateTime());
        bucketMetaData.put("VersioningEnabled" , bucket.versioningEnabled());

        if (bucket.getLifecycleRules() != null) {

            for (BucketInfo.LifecycleRule rule : bucket.getLifecycleRules()) {
                bucketMetaData.put("LifeCycleRule", rule);
            }
        }

        return bucketMetaData;

    }

    public List<String> listDatasets(String projectId) {
        List<String> datasetList= new ArrayList<>();
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(configParser.inputParam.get("serviceAccount."+projectId)));
            BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();

            Page<Dataset> datasets = bigquery.listDatasets(projectId, BigQuery.DatasetListOption.pageSize(100));
            if (datasets == null) {
                System.out.println("Dataset does not contain any models");
                return datasetList;
            }

            datasets.iterateAll().forEach(dataset -> {
                datasetList.add(dataset.getDatasetId().getDataset());
            });

        } catch (BigQueryException | FileNotFoundException e) {
            System.out.println("Project does not contain any datasets \n" + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datasetList;

    }

    public List<String> listTables(String projectId, String datasetName) {
        List<String> listTable = new ArrayList<>();

        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(configParser.inputParam.get("serviceAccount."+projectId)));
            BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();

            DatasetId datasetId = DatasetId.of(projectId, datasetName);
            Page<Table> tables = bigquery.listTables(datasetId, BigQuery.TableListOption.pageSize(100));

            tables.iterateAll().forEach(table -> {
                listTable.add(table.getTableId().getTable());
            });

            System.out.println("Tables listed successfully.");
        } catch (BigQueryException | FileNotFoundException e) {
            System.out.println("Tables were not listed. Error occurred: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listTable;
    }

    public Table getBigQueryTableDetails(String projectId, String datasetName, String tableName){

        Table table=null;

        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(configParser.inputParam.get("serviceAccount."+projectId)));
            BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();
            TableId tableId = TableId.of(projectId, datasetName, tableName);
            table = bigquery.getTable(tableId);


        } catch (BigQueryException | FileNotFoundException e) {
            System.out.println("Table not retrieved. \n" + e.toString());
        } catch (IOException e) {
            System.out.println("Unable to read auth file");
        }
        return table;
    }

    public Boolean validateGcpCredFile(String serviceAccountFile) {
        try {

            InputStream targetStream = new ByteArrayInputStream(serviceAccountFile.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);

        } catch (Exception e) {
            System.out.println("Unable to establish connection with authfile provided");
            return false;
        }

        return true;

    }

}