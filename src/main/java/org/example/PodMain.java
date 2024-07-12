package org.example;
import com.google.gson.Gson;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;

import java.io.IOException;

public class PodMain {
    public static void main(String[] args) {
        try {
            // Kubernetes API 클라이언트 초기화
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            // CoreV1Api 인스턴스 생성
            CoreV1Api api = new CoreV1Api();
            // 모든 네임스페이스의 Pod 목록을 가져옴
            Integer limit = 20;
            String _continue = null;
            String fieldSelector = "spec.nodeName="+"minikube";
            while (true) {
                V1PodList pods;
                pods = api.listPodForAllNamespaces(null, _continue, fieldSelector, null, limit, null, null, null, null, null);
                for (V1Pod pod : pods.getItems()) {
                    System.out.println(pod.getMetadata().getName());
                }
                if(pods != null && pods.getMetadata() != null && pods.getMetadata().getContinue() != null && !pods.getMetadata().getContinue().isEmpty()) {
                    _continue = pods.getMetadata().getContinue();
                    System.out.println("T_continue="+_continue.toString());
                }else {
                    System.out.println("F_continue="+_continue.toString());
                    System.out.println("F_pods="+pods.getMetadata());
                    break;
                }
            }
            // Gson 인스턴스 생성
//            Gson gson = new Gson();

            // V1PodList 객체를 JSON 문자열로 변환
//            String jsonOutput = gson.toJson(podList);

            // JSON 결과 출력
//            System.out.println(podList);
        } catch (ApiException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}