package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.BatchV1beta1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;

import java.io.IOException;

public class CronJobMain {
    public static void main(String[] args) throws IOException{
        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);

            BatchV1Api batchV1Api = new BatchV1Api();
            V1CronJobList cronJobList = batchV1Api.listCronJobForAllNamespaces(null, null, null,null,null,null,null,null,null,null);

            // Print Job details
            for(V1CronJob cj: cronJobList.getItems()){

                System.out.println("name:"+cj.getMetadata().getName());
            }
//        System.out.println("Job : " + cronJob.getMetadata().getOwnerReferences());
//        System.out.println("Job Name: " + cronJob.getSpec().get);
        }catch(ApiException e){
            System.out.println("error:"+e.getMessage());
        }
        }
    }
//}
