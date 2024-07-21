package org.ignis.backend.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import org.ignis.backend.cluster.*;
import org.ignis.properties.IKeys;
import org.ignis.properties.IProperties;

import static org.ignis.backend.ui.ComplexJsonMapper.*;


public class DBUpdateService{

    private static String  BASE_URL = "http://172.17.0.1:5038/api/IClusterPrueba/";

    public DBUpdateService(String baseUrl) {
        BASE_URL = baseUrl + "/api/IClusterPrueba/";
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String insertJob(IProperties properties) throws IOException {
        String endpoint = "InsertJob";
        String url = BASE_URL + endpoint;

        //ClusterPayload payload = new ClusterPayload(jobId, cluster);
        //String jsonPayload = objectMapper.writeValueAsString(payload);
        //custom objectMapper
        String jsonPayload = jobPostBuilder(properties);
        return sendPostRequest(url, jsonPayload);
    }

    public static String upsertCluster(String jobId, ICluster cluster) throws IOException {
        String endpoint = "UpsertCluster";
        String url = BASE_URL + endpoint;

        //ClusterPayload payload = new ClusterPayload(jobId, cluster);
        //String jsonPayload = objectMapper.writeValueAsString(payload);
        //custom objectMapper
        String jsonPayload = clusterPostBuilder(jobId, cluster);
        return sendPostRequest(url, jsonPayload);
    }

    public static String upsertWorker(String jobId, long clusterId, IWorker worker) throws IOException {
        String endpoint = "UpsertWorker";
        String url = BASE_URL + endpoint;

        //WorkerPayload payload = new WorkerPayload(jobId, clusterId, worker);
        //String jsonPayload = objectMapper.writeValueAsString(payload);
        String jsonPayload = workerPostBuilder(jobId, clusterId, worker);
        return sendPostRequest(url, jsonPayload);
    }

    public static String upsertContainer(String jobId, long clusterId, IContainer container) throws IOException {
        String endpoint = "UpsertContainer";
        String url = BASE_URL + endpoint;

        String jsonPayload = containerPostBuilder(jobId, clusterId, container);
        return sendPostRequest(url, jsonPayload);
    }
    public static String upsertManyContainers(String jobId, long clusterId, List<IContainer> containers) throws IOException {
        String endpoint = "UpsertMultipleContainers";
        String url = BASE_URL + endpoint;

        String jsonPayload = containersPostBuilder(jobId, clusterId, containers);
        return sendPostRequest(url, jsonPayload);
    }
    public static String destroyManyContainers(String jobId, long clusterId, List<Long> containerids) throws IOException {
        String endpoint = "DeleteMultipleContainers";
        String url = BASE_URL + endpoint;

        String jsonPayload = containersDestroyBuilder(jobId, clusterId, containerids);
        return sendDeleteRequest(url, jsonPayload);
    }

    public static String destroyJob(String jobId, long clusterId)throws IOException{
        String endpoint = "DestroyJob";
        String url = BASE_URL + endpoint  + "/" + jobId;
        return sendDeleteRequest(url);
    }
    public static String destroyCluster(String jobId, long clusterId)throws IOException{
        String endpoint = "DestroyCluster";
        String url = BASE_URL + endpoint  + "/" + jobId+ "/" + clusterId;
        return sendDeleteRequest(url);
    }
    public static String destroyWorker(String jobId, long clusterId, long workerId)throws IOException{
        String endpoint = "DestroyWorker";
        String url = BASE_URL + endpoint  + "/" + jobId+ "/" + clusterId + "/" + workerId;
        return sendDeleteRequest(url);
    }
    private static String sendDeleteRequest(String url) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(url);
            try (CloseableHttpResponse response = client.execute(httpDelete)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
    private static String sendDeleteRequest(String url, String jsonPayload) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
            httpDelete.setHeader("Content-Type", "application/json");
            httpDelete.setEntity(new StringEntity(jsonPayload));
            try (CloseableHttpResponse response = client.execute(httpDelete)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
    private static String sendPostRequest(String url, String jsonPayload) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonPayload));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    /*private static class ClusterPayload {
        public String jobId;
        public ICluster cluster;

        public ClusterPayload(String jobId, ICluster cluster) {
            this.jobId = jobId;
            this.cluster = cluster;
        }
    }*/

    /*private static class WorkerPayload {
        public String jobId;
        public String clusterId;
        public IWorker worker;

        public WorkerPayload(String jobId, String clusterId, IWorker worker) {
            this.jobId = jobId;
            this.clusterId = clusterId;
            this.worker = worker;
        }
    }*/


}