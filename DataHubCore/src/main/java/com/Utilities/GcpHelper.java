package com.Utilities;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.paging.Page;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import com.google.cloud.devtools.containeranalysis.v1.ContainerAnalysisClient;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.google.cloud.storage.*;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import io.grafeas.v1.GrafeasClient;
import io.grafeas.v1.Occurrence;
import io.grpc.LoadBalancerRegistry;
import io.grpc.internal.PickFirstLoadBalancerProvider;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GcpHelper {

    public GcpHelper(String projectId) {
        this.projectId = projectId;
        LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, "service_account", "latest");
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            serviceKey = response.getPayload().getData().toStringUtf8();
            bigquery = getBigQueryService(projectId);
        } catch (Exception e) {
            Log.error(e.toString());
        }
    }

    private String projectId;
    private String serviceKey;
    private BigQuery bigquery;

    public GoogleCredentials getGcpCredentials() {
        GoogleCredentials credentials = null;
        try {
            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            credentials = GoogleCredentials.fromStream(targetStream);
        } catch (Exception e) {
            Log.error(e.toString());
        }
        return credentials;
    }

    public List<HashMap<String, Object>> queryDataSet(String sql, List<String> columns) {
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
            Log.error(e.toString());
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

        List<HashMap<String, Object>> outputData = new ArrayList<>();

        // Print all pages of the results.
        for (FieldValueList row : result.iterateAll()) {

            columns.forEach(item -> {
                HashMap<String, Object> data = new HashMap<>();
                data.put(item, row.get(item).getRecordValue().get(0).getStringValue());
                outputData.add(data);
            });


        }
        return outputData;
    }

    /**
     * This method will upload the given file to the GCP bucket
     *
     * @param bucketName
     * @param objectName
     * @param filePath
     */
    public void uploadObject(String bucketName, String objectName, String filePath) {

        Storage storage = null;
        try {

            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);

            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create the connection with GCP", e);
        }
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try {
            storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.out.println("unable to read file");
        }

        Log.info(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    /**
     * Publishing data to PubSub topic
     *
     * @param projectId
     * @param topicId
     * @throws IOException
     * @throws InterruptedException
     */
    public void publishToPubSub(String projectId, String topicId, JSONObject message)
            throws IOException, InterruptedException {
        LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {

            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();

            // List<String> messages = Arrays.asList("first message", "second message");

            ByteString data = ByteString.copyFromUtf8(message.toJSONString());
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
                                Log.info(apiException.getStatusCode().getCode().toString());

                            }
                            Log.error("Error publishing message : " + message.toString());
                        }

                        @Override
                        public void onSuccess(String messageId) {
                            // Once published, returns server-assigned message ids (unique within the topic)
                            Log.info("Message Published Successfully with id: " + messageId);

                        }
                    },
                    MoreExecutors.directExecutor());

        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

    public void getVulnerabilityScanReport(String projectId, String resourceUrl) throws IOException {

        resourceUrl = "https://gcr.io/repd-e-eng-01/bdpmgr_7.0.3@sha256:2c070c982f6b569625bb7b643d72f55da716f3dc1ec3420a7a2f4042bf65796a";
        projectId = "repd-e-eng-01";


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

    public List<String> listBucketObjects(String bucketName) {

        Storage storage = null;
        try {
            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (Exception e) {
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

    public String readFileFromBucket(String bucketName, String fileName) {
        Storage storage = null;
        try {
            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (Exception e) {
            System.out.println("Unable to get authentication json");
        }
        Blob blob = storage.get(bucketName, fileName);

        String fileContent = new String(blob.getContent());
        return fileContent;
    }

    public HashMap<String, Object> getBucketDetails(String bucketName) {

        Storage storage = null;
        try {
            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (Exception e) {
            System.out.println("Unable to get authentication json");
        }

        Bucket bucket =
                storage.get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.values()));

        HashMap<String, Object> bucketMetaData = new HashMap<>();
        bucketMetaData.put("BucketName", bucket.getName());
        bucketMetaData.put("DefaultEventBasedHold", bucket.getDefaultEventBasedHold());
        bucketMetaData.put("DefaultKmsKeyName", bucket.getDefaultKmsKeyName());
        bucketMetaData.put("Id", bucket.getGeneratedId());
        bucketMetaData.put("IndexPage", bucket.getIndexPage());
        bucketMetaData.put("Location", bucket.getLocation());
        bucketMetaData.put("LocationType", bucket.getLocationType());
        bucketMetaData.put("Metageneration", bucket.getMetageneration());
        bucketMetaData.put("NotFoundPage", bucket.getNotFoundPage());
        bucketMetaData.put("RetentionEffectiveTime", bucket.getRetentionEffectiveTime());
        bucketMetaData.put("RetentionPeriod", bucket.getRetentionPeriod());
        bucketMetaData.put("RetentionPolicyIsLocked", bucket.retentionPolicyIsLocked());
        bucketMetaData.put("RequesterPays", bucket.requesterPays());
        bucketMetaData.put("SelfLink", bucket.getSelfLink());
        bucketMetaData.put("StorageClass", bucket.getStorageClass().name());
        bucketMetaData.put("TimeCreated", bucket.getCreateTime());
        bucketMetaData.put("VersioningEnabled", bucket.versioningEnabled());

        if (bucket.getLifecycleRules() != null) {

            for (BucketInfo.LifecycleRule rule : bucket.getLifecycleRules()) {
                bucketMetaData.put("LifeCycleRule", rule);
            }
        }

        return bucketMetaData;

    }

    public List<String> listDatasets() {
        List<String> datasetList = new ArrayList<>();
        try {
            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);
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

    public List<String> listTables(String datasetName) {
        List<String> listTable = new ArrayList<>();

        try {
            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);
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

    public Table getBigQueryTableDetails(String datasetName, String tableName) {

        Table table = null;

        try {
            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);
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

    /**
     * This method is used to load data from local CSV to the big query table
     *
     * @param datasetName
     * @param tableName
     * @param csvPath
     * @param formatOptions
     */
    public void loadCsvFileToBigqueryTable(String datasetName, String tableName, Path csvPath, FormatOptions formatOptions) {
        try {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests.
            InputStream targetStream = new ByteArrayInputStream(serviceKey.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream);
            BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();

            TableId tableId = TableId.of(projectId, datasetName, tableName);

            WriteChannelConfiguration writeChannelConfiguration =
                    WriteChannelConfiguration.newBuilder(tableId).setFormatOptions(formatOptions).build();

            // The location and JobName must be specified; other fields can be auto-detected.
            String jobName = "jobId_" + UUID.randomUUID().toString();
            JobId jobId = JobId.newBuilder().setLocation("us").setJob(jobName).build();

            // Imports a local file into a table.
            try (TableDataWriteChannel writer = bigquery.writer(jobId, writeChannelConfiguration);
                 OutputStream stream = Channels.newOutputStream(writer)) {

                // This example writes CSV data from a local file,
                // but bytes can also be written in batch from memory.
                // In addition to CSV, other formats such as
                // Newline-Delimited JSON (https://jsonlines.org/) are
                // supported.
                Files.copy(csvPath, stream);
            }

            // Get the Job created by the TableDataWriteChannel and wait for it to complete.
            Job job = bigquery.getJob(jobId);
            Job completedJob = job.waitFor();
            if (completedJob == null) {
                System.out.println("Job not executed since it no longer exists.");
                return;
            } else if (completedJob.getStatus().getError() != null) {
                throw new RuntimeException("BigQuery was unable to load local file to the table due to an error: \n" +
                        job.getStatus().getError());
            }
            // Get output status
            JobStatistics.LoadStatistics stats = job.getStatistics();
            // logger.info("Successfully loaded %d rows. \n", stats.getOutputRows());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load the data from csv to BigQuery table ", e);
        }
    }

    /**
     * This method executes batch query to the big query table
     *
     * @param query
     */
    public TableResult queryBatch(String query) {

        if (StringUtils.isEmpty(query) || StringUtils.isEmpty(projectId)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            BigQuery bigquery = getBigQueryService(projectId);
            QueryJobConfiguration queryConfig =
                    QueryJobConfiguration.newBuilder(query)
                            // Run at batch priority, which won't count toward concurrent rate limit.
                            .setPriority(QueryJobConfiguration.Priority.BATCH)
                            .build();

            TableResult results = bigquery.query(queryConfig);
            Log.info("Query is getting executed ...\n" + query);
            results.iterateAll()
                    .forEach(row -> row.forEach(val -> System.out.printf("%s,", val.toString())));
            Log.info("Query batch performed successfully.");
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute the query  " + query, e);
        }
    }


    /**
     * This method is used to execute the script and stored procedures in BigQuery
     *
     * @param script
     */
    public void queryScript(String script) {

        if (StringUtils.isEmpty(script) || StringUtils.isEmpty(projectId)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }
        try {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests.
            BigQuery bigquery = getBigQueryService(projectId);

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(script).build();
            Job createJob = bigquery.create(JobInfo.of(queryConfig));
            // Wait for the whole script to finish.
            Log.info("Waiting for the script to finish ....!!");
            JobInfo jobInfo = createJob.waitFor();

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute the Script  " + script, e);
        }
    }

    /**
     * This method will execute the query with the  given params
     */
    public void queryWithAdditionalParameter(String query, int param) {

        if (StringUtils.isEmpty(query) || StringUtils.isEmpty(projectId)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests.
            BigQuery bigquery = getBigQueryService(projectId);
            // Note: Standard SQL is required to use query parameters.
            QueryJobConfiguration queryConfig =
                    QueryJobConfiguration.newBuilder(query)
                            .addPositionalParameter(QueryParameterValue.int64(param))
                            .build();

            bigquery.query(queryConfig);
            Log.info("Query with positional parameters performed successfully." + query);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute the query  " + query, e);
        }
    }


    /**
     * This method is used to create the dataset in the
     *
     * @param datasetName
     */
    public void createDataset(String datasetName) {

        if (StringUtils.isEmpty(datasetName) || StringUtils.isEmpty(projectId)) {
            throw new IllegalArgumentException("Invalid Arguments..!!");
        }

        try {
            if (!isDataSetExists(projectId, datasetName)) {
                Log.info("Creating dataset " + datasetName);
                BigQuery bigquery = getBigQueryService(projectId);
                DatasetInfo datasetInfo = DatasetInfo.newBuilder(Preconditions.checkNotNull(datasetName)).build();
                Dataset newDataset = bigquery.create(datasetInfo);
                String newDatasetName = newDataset.getDatasetId().getDataset();
                Log.info(newDatasetName + " created successfully");
            } else {
                Log.info("Dataset " + datasetName + " already exists..!!");
            }
        } catch (BigQueryException e) {
            throw new RuntimeException("Failed to create the dataset " + datasetName, e);
        }
    }

    public boolean isDataSetExists(String projectId, String datasetName) {
        boolean isDataSetExists = false;
        try {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests.
            BigQuery bigquery = getBigQueryService(projectId);
            DatasetId datasetId = DatasetId.of(projectId, datasetName);
            isDataSetExists = (bigquery.getDataset(datasetId) == null) ? false : true;
        } catch (BigQueryException e) {
            Log.error("Failed to check the database existence " + datasetName);
        }
        return isDataSetExists;
    }

    /**
     * This method will return the big query client that will be used to send requests.
     * This client only needs to be created once, and can be reused for multiple requests
     *
     * @param projectId
     * @return
     */
    private BigQuery getBigQueryService(String projectId) {
        GoogleCredentials credentials = null;
        try {
            credentials = getGcpCredentials();
            return BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get the BigQuery Service for Project Id " + projectId);
        }
    }
}