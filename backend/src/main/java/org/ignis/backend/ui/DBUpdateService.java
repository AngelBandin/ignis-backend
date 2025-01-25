package org.ignis.backend.ui;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import org.ignis.backend.cluster.*;
import org.ignis.properties.IProperties;

import static org.ignis.backend.ui.ComplexJsonMapper.*;


public class DBUpdateService{

    private static String  BASE_URL = "http://172.17.0.1:5038/api/v1/";
    private static boolean active = true;

    public static void setupDBUpdateService(boolean ui_up ,String url, int port) {
        active = ui_up;
        BASE_URL = url +":"+port+ "/api/v1/";
    }


    public static String insertJob(IProperties properties, IContainer driver) throws IOException {

        if(!active) {
            return "";
        }
        String endpoint = "InsertJob";
        String url = BASE_URL + endpoint;
        String jsonPayload = jobPostBuilder(properties,driver);
        return sendPostRequest(url, jsonPayload);
    }

    public static String upsertCluster(String jobId, ICluster cluster) throws IOException {
        if(!active) {
            return "";
        }
        String endpoint = "UpsertCluster";
        String url = BASE_URL + endpoint;
        String jsonPayload = clusterPostBuilder(jobId, cluster);
        return sendPostRequest(url, jsonPayload);
    }

    public static String upsertWorker(String jobId, long clusterId, IWorker worker) throws IOException {
        if(!active) {
            return "";
        }
        String endpoint = "UpsertWorker";
        String url = BASE_URL + endpoint;

        String jsonPayload = workerPostBuilder(jobId, clusterId, worker);
        return sendPostRequest(url, jsonPayload);
    }

    public static String upsertManyContainers(String jobId, long clusterId, List<IContainer> containers) throws IOException {
        if(!active) {
            return "";
        }
        String endpoint = "UpsertMultipleContainers";
        String url = BASE_URL + endpoint;

        String jsonPayload = containersPostBuilder(jobId, clusterId, containers);
        return sendPostRequest(url, jsonPayload);
    }

    public static String finishJob(IProperties properties) throws IOException {

        if(!active) {
            return "";
        }
        String endpoint = "UpdateJob";
        String url = BASE_URL + endpoint;


        String jsonPayload = jobFinishBuilder(properties);
        return sendPutRequest(url, jsonPayload);
    }

    private static String sendPostRequest(String url, String jsonPayload) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonPayload));
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return "\nResponse:"+EntityUtils.toString(response.getEntity());
            }
        }
    }
    private static String sendPutRequest(String url, String jsonPayload) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(url);
            httpPut.setHeader("Content-Type", "application/json");
            httpPut.setEntity(new StringEntity(jsonPayload));
            try (CloseableHttpResponse response = client.execute(httpPut)) {
                return "\nResponse:"+EntityUtils.toString(response.getEntity());
            }
        }
    }

}