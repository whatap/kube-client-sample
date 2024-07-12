package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobMain {
    public static void main(String[] args) throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        BatchV1Api batchV1Api = new BatchV1Api();
        V1JobList jobList =  batchV1Api.listNamespacedJob("default",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        V1Job job = batchV1Api.readNamespacedJob("my-job-test", "default", null);
        for (V1Job j:  jobList.getItems()){
            System.out.println("Job_Active: " + j.getStatus().getConditions());
            for (V1OwnerReference owner: j.getMetadata().getOwnerReferences()){
                System.out.println("owner: " + owner.getKind());
            }

        }
        // Print Job details
//        System.out.println("Job Name: " + job.getMetadata().getName());
//        System.out.println("Status: " + job.getStatus());
        }
    }
//}
