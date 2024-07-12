package org.example;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.CoreV1Event;
import io.kubernetes.client.openapi.models.CoreV1EventList;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class EventWatchGpt {
    static String lastKnownResourceVersion = "35312";
    public static void main(String[] args) {
        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            CoreV1Api api = new CoreV1Api();

            while (true) {
                System.out.println("Watching events...");
                boolean deleteEventOccur = false;
                try (Watch<CoreV1Event> watch = Watch.createWatch(
                        client,
                        api.listEventForAllNamespacesCall(null, null, null, null, null, null, lastKnownResourceVersion, null, null, true, null),
                        new TypeToken<Watch.Response<CoreV1Event>>() {}.getType())) {

                    for (Watch.Response<CoreV1Event> item : watch) {
                        CoreV1Event event = item.object;

                        if (event == null) {
                            System.out.println("event is null..."+item.status);
                            CoreV1EventList EventList =  api.listEventForAllNamespaces(null, null, null, null, null,
                                    null, null, null, null, false);
                            lastKnownResourceVersion = EventList.getMetadata().getResourceVersion();
                            continue; // Skip if the event is null
                        }
                        V1ObjectMeta meta = event.getMetadata();
                        lastKnownResourceVersion = meta.getResourceVersion();
                        //System.out.println("Event type=" + item.type + "//Resource Version=" + lastKnownResourceVersion);

                        switch (item.type) {
                            case "ADDED":
//                                lastKnownResourceVersion= "1";
                                System.out.println("added event..."+event.getMetadata().getName());
//                                System.out.println("ADDED"+"//a.involvedObject.name="+meta.getName()+"//reason="+event.getReason()+"//eventtime="+event.getMetadata());
                                break;
                            case "MODIFIED":
//                                System.out.println("An existing event was modified.");
                                break;
                            case "DELETED":
                                System.out.println("deleted eventRRRR..."+event.getMetadata().getResourceVersion());
                                //System.out.println("DELETED"+"//a.involvedObject.name="+meta.getName()+"//reason="+event.getReason()+"//kind="+event.getInvolvedObject().getKind());
                                deleteEventOccur = true;
                                break;
                            default:
                                System.out.println("Unknown event type.");
                                break;
                        }
                        if (deleteEventOccur){
                            lastKnownResourceVersion = event.getMetadata().getResourceVersion();
                            System.out.println("deleted event...lastKnownResourceVersion="+lastKnownResourceVersion);
                            watch.close();
                            break;
                        }
                    }
                } catch (ApiException ex) {
                    System.out.printf("API Exception: message=%s, code=%s\n", ex.getMessage(), ex.getCode());
                    lastKnownResourceVersion = null; // Reset and resync on error
                }catch (SocketTimeoutException e) {
                    System.out.printf("Socket TimeoutException: message=%s, code=%s\n", e.getMessage(), e.getCause());
                }
                catch (IOException ex) {
                    System.out.println("IO Exception: " + ex.getMessage());
                    break; // Exit the loop on IO exceptions
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }finally {
                    System.out.println("hello");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}