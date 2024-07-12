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
import io.kubernetes.client.openapi.models.V1WatchEvent;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import io.kubernetes.client.util.Watch.Response;
import whatap.util.ThreadUtil;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

public class eventWatchTest {

    public static String lastKnownResourceVersion=null;

    public static void test() {
        System.out.println("delete");
    }


    public static void main(String[] args) throws Exception {
        while (true) {
            try {
                process();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("here");
            Thread.sleep(500);
        }
    }

    public static void process() throws ApiException, IOException {
        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            CoreV1Api api = new CoreV1Api();
            //String lastKnownResourceVersion = null;

            while (true) {
                System.out.println("start="+lastKnownResourceVersion);
                if (lastKnownResourceVersion == null) {
                    V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null,
                            null, null, null, null, false);
                    V1ListMeta v1ListMeta = podList.getMetadata();
                    if (v1ListMeta != null){
                        lastKnownResourceVersion = v1ListMeta.getResourceVersion();
                    }
//                    for (V1Pod pod : podList.getItems()){
//                        System.out.println("Watching event...");
//                    }
                }
                String knownResourceVersion = "12345";
                try (Watch<V1Pod> watch = Watch.createWatch(
                        client,
                        api.listPodForAllNamespacesCall(null, null, null,  null, null,  null, knownResourceVersion, null, null, true, null),
                        new TypeToken<Watch.Response<V1Pod>>() {}.getType())) {

                    for (Watch.Response<V1Pod> event : watch) {
                        V1Pod pod = event.object;
//                        V1ObjectMeta meta = pod.getMetadata();
//                        System.out.println("eventHandling...");
                        switch (event.type) {
//                            case "BOOKMARK":
//                                lastKnownResourceVersion = meta.getResourceVersion();
//                                System.out.println("BOOKMARK/lastKnownResourceVersion="+lastKnownResourceVersion);
//                                break;
                            case "ADDED":
                                System.out.println("ADDED...");
                                break;
                            case "MODIFIED":
                                System.out.println("MODIFIED...");
                                break;
                            case "DELETED":
                                System.out.println("DELETED...");
                                break;
//                            default:
//                                System.out.println("event.type="+event.type);
//                                break;
                        }
                        System.out.println("switchOut="+knownResourceVersion);
//                        if (event == null) {
//                            System.out.println("3423324event is null...");
//                            continue; // Skip if the event is null
//                        }
                    }
                } catch (ApiException ex) {
                    System.out.printf("API Exception: message=%s, code=%s\n", ex.getMessage(), ex.getCode());
                    knownResourceVersion = null; // Reset and resync on error
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