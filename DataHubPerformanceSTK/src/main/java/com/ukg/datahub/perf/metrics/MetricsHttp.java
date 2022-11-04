package com.ukg.datahub.perf.metrics;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Arrays;

import com.Utilities.GcpHelper;
import com.google.auth.oauth2.*;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.http.HttpCredentialsAdapter;

public class MetricsHttp {
    private final static String BASE_MQL = "{\"query\": \"fetch %s | metric \'%s\'| within d'%s',d'%s'\"}";
    private final static String BASE_URL = " https://monitoring.googleapis.com/v3/projects/%s/timeSeries:query";
    private final static String filePath = "C:\\_BDP\\authentication\\repd-e-eng-12-0dac88cddf39.json";

    final static String[] CF_USER_MEMORY_BYTES = { "cloud_function",
            "cloudfunctions.googleapis.com/function/user_memory_bytes" };

    // final static String[] PS_OLD_UNACK_MSG_BYREGION = { "pubsub_topic",
    // "pubsub.googleapis.com/topic/oldest_unacked_message_age_by_region" };

    /**
     * Thsis method will do a post request
     * @param projectId
     * @param metric
     * @param fromDate
     * @param toDate
     * @return
     */
    public static String doRequestPost(String projectId, String[] metric, String fromDate, String toDate) {

        StringBuffer content = new StringBuffer();
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(makePostRequest(genMetricURL(projectId),
                    projectId, genMetricBody(metric, StringMQLDate(fromDate), StringMQLDate(toDate))).getContent()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return content.toString();
    }

    public static HttpResponse makeGetRequest(String serviceUrl, String filePath) throws IOException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(filePath))
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        GenericUrl genericUrl = new GenericUrl(serviceUrl);
        HttpCredentialsAdapter adapter = new HttpCredentialsAdapter(credentials);
        HttpTransport transport = new NetHttpTransport();
        HttpRequest request = transport.createRequestFactory(adapter).buildGetRequest(genericUrl);
        return request.execute();
    }

    public static HttpResponse makePostRequest(String serviceUrl, String projectId, String content) throws IOException {

        GcpHelper gcpHelper = new GcpHelper(projectId);
        GoogleCredentials credentials = gcpHelper.getGcpCredentials();
        credentials = credentials.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        GenericUrl genericUrl = new GenericUrl(serviceUrl);
        HttpCredentialsAdapter adapter = new HttpCredentialsAdapter(credentials);
        HttpTransport transport = new NetHttpTransport();
        HttpRequest request = transport.createRequestFactory(adapter).buildPostRequest(genericUrl,
                ByteArrayContent.fromString("application/json", content));
        return request.execute();
    }

    public static String genMetricBody(String[] metricName, String fromDate, String toDate) {
        return String.format(BASE_MQL, metricName[0], metricName[1], fromDate, toDate);
    }

    public static String genMetricURL(String projectId) {
        return String.format(BASE_URL, projectId);

    }

    public static String StringMQLDate(String dateString) {
        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"); // "2021-09-02 16:35:56.129771"
        DateFormat outputDateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss"); // "2021/09/03-12:0:00"

        try {
            return outputDateFormat.format(inputDateFormat.parse(dateString));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Please provide date in format: yyyy-MM-dd HH:mm.ss.SSSSSS");
        }

    }
}