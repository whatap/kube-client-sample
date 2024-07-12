package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Eviction;
import io.kubernetes.client.util.Config;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PodEviction {
    public static void main(String[] args) throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1Eviction res = api.createNamespacedPodEviction("test", "default",null,null,null,null,null);
        System.out.println("res.metadata():"+ res.toString());


        }
    }
//}
