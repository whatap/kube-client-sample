package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Watch.Response;
import io.kubernetes.client.openapi.models.V1PodList;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import okhttp3.Call;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PendingPodTest {

    public static boolean isPodStuckInPending(V1Pod pod) {
        Instant creationTimestamp = pod.getMetadata().getCreationTimestamp().toInstant();
        long minutesSinceCreation = java.time.Duration.between(creationTimestamp, Instant.now()).toMinutes();
        return minutesSinceCreation > 5; // If more than 5 minutes have passed, consider it stuck.
    }
    public static void test(){
        System.out.println("delete");
    }
    public static void main(String[] args) {
        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            CoreV1Api api = new CoreV1Api();
            String lastKnownResourceVersion = null;
            String fieldSelector = null;

            while (true) {
                if (lastKnownResourceVersion == null) {
                    V1PodList podList = api.listPodForAllNamespaces(null, null, null, fieldSelector, null,
                            null, null, null, null, false);
                    lastKnownResourceVersion = podList.getMetadata().getResourceVersion();
                }
                Instant now = Instant.now();
                System.out.println("create Watch // time="+now+"//lastKnownResourceVersion="+lastKnownResourceVersion);
                try (Watch<V1Pod> watch = Watch.createWatch(client, api.listPodForAllNamespacesCall(null, null, null, fieldSelector, null, null, null, null, 30, true, null), new TypeToken<Response<V1Pod>>() {}.getType())) {
                    for (Response<V1Pod> event : watch) {
                        V1Pod pod = event.object;
                        V1ObjectMeta meta = pod.getMetadata();
                        lastKnownResourceVersion = meta.getResourceVersion();
                        System.out.println("type="+event.type+"//resourceVersion="+lastKnownResourceVersion);
                        switch (event.type) {
                            case "ADDED":
                                System.out.println("ADDED//podStatus="+pod.getStatus().getPhase()+"//podReason="+pod.getStatus().getReason());
                                continue;
                            case "MODIFIED":
                                System.out.println("MODIFIED//podStatus="+pod.getStatus().getPhase()+"//podReason="+pod.getStatus().getReason());
                                continue;
                            case "DELETED":
                                System.out.println("DELETED//podStatus="+pod.getStatus().getPhase()+"//podReason="+pod.getStatus().getReason());
                                test();
                        }
                    }
                }catch (ApiException ex) {
                    System.out.printf("API Exception: message=%s , code=%s ", ex.getMessage(), ex.getCode());
                    lastKnownResourceVersion = null; // Reset and resync on error
                } catch (IOException ex) {
                    System.out.println("IO Exception: " + ex.getMessage());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}