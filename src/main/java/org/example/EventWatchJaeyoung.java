package org.example;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.CoreV1Event;
import io.kubernetes.client.openapi.models.V1ListMeta;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;

import java.io.IOException;

public class EventWatchJaeyoung {
    public static void main(String[] args) {
        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            CoreV1Api api = new CoreV1Api();
            String lastKnownResourceVersion = null;

            while (true) {
                System.out.println("start="+lastKnownResourceVersion);
                if (lastKnownResourceVersion == null) {
                    V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null,
                            null, null, null, null, false);
                    V1ListMeta v1ListMeta = podList.getMetadata();
                    if (v1ListMeta != null){
                        lastKnownResourceVersion = v1ListMeta.getResourceVersion();
                    }
                    for (V1Pod pod : podList.getItems()){
                        System.out.println("Watching event...");
                    }
                }
                try (Watch<CoreV1Event> watch = Watch.createWatch(
                        client,
                        api.listEventForAllNamespacesCall(null, null, null,  null, null,  null, lastKnownResourceVersion, null, null, true, null),
                        new TypeToken<Watch.Response<CoreV1Event>>() {}.getType())) {

                    for (Watch.Response<CoreV1Event> item : watch) {
                        CoreV1Event event = item.object;
//                        if (event == null) {
//                            System.out.println("3423324event is null...");
//                            continue; // Skip if the event is null
//                        }
                        V1ObjectMeta meta = event.getMetadata();
                        lastKnownResourceVersion = meta.getResourceVersion();
                        System.out.println("Event type=" + item.type + "//Resource Version=" + lastKnownResourceVersion);
                        throw new NullPointerException("강제적으로 발생된 IOException");
                    }
                } catch (ApiException ex) {
                    System.out.printf("API Exception: message=%s, code=%s\n", ex.getMessage(), ex.getCode());
                    lastKnownResourceVersion = null; // Reset and resync on error
                } catch (IOException ex) {
                    System.out.println("IO Exception: " + ex.getMessage());
                    break; // Exit the loop on IO exceptions
                }
            }
        } catch (Exception ex) {
            System.out.println("TEST: " + ex.getMessage());
        }
    }
}