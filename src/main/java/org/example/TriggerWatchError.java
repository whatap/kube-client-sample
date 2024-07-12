package org.example;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.CoreV1Event;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;

public class TriggerWatchError {
    public static void main(String[] args) {
        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            CoreV1Api api = new CoreV1Api();

            String invalidResourceVersion = "12345"; // Invalid resource version
            while (true){
                try (Watch<V1Pod> watch = Watch.createWatch(
                        client,
                        api.listPodForAllNamespacesCall(
                                null, null, null, null, null, null, invalidResourceVersion, null, null, true, null),
                        new TypeToken<Watch.Response<V1Pod>>() {}.getType())) {

                    for (Watch.Response<V1Pod> item : watch) {
                        switch (item.type){
                            case "ADDED":
                                System.out.println("ADDED...");
                                break;
                            case "MODIFIED":
                                System.out.println("MODIFIED...");
                                break;
                            case "DELETED":
                                System.out.println("DELETED...");
                                break;
//                        default:
//                            System.out.println("item.type="+item.type);
//                            break;
                        }
                        System.out.println("Event: " + item.status.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println("Error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
