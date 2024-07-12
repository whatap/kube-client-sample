package org.example;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import io.kubernetes.client.util.Watch.Response;
import okhttp3.Call;

import java.io.IOException;
import java.time.OffsetDateTime;

public class PendingPodGetList {
    public static void process(V1PodList pods){
        for(V1Pod pod: pods.getItems()){
            System.out.println(pod.getMetadata().getName());
        }
    }
    public static void main(String[] args) throws IOException {
        // 1. Kubernetes 클라이언트 설정
        ApiClient client = Config.defaultClient();

        Configuration.setDefaultApiClient(client);
        CoreV1Api api = new CoreV1Api();
        try{
            while(true){
                String fieldSelector = null;
                V1PodList pods = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                if(pods != null) {
                    process(pods);
                }
                System.out.println("sleep 5\n");
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



//            try (Watch<V1Pod> watch = api.create(
//                    new ListOptions.Builder<V1Pod>()
//                            .fieldSelector("status.phase=Pending") // Pending 상태 필터
//                            .build())) {
//                watch.forEach(response -> {
//                    V1Pod pod = response.object;
//                    System.out.printf("Detected pending pod: %s in namespace: %s%n",
//                            pod.getMetadata().getName(),
//                            pod.getMetadata().getNamespace());
//
//                    // 여기서 추가적인 데이터 수집 로직을 구현할 수 있습니다.
//                    // 예: pod.getMetadata(), pod.getStatus() 등에서 필요한 정보를 추출
//                });
//            }
        }
    }
