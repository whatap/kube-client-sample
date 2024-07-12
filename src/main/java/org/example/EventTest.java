package org.example;
import com.google.gson.Gson;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.CoreV1EventList;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;

public class EventTest {
    public static void main(String[] args) {
        try {
            // Kubernetes API 클라이언트 초기화
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);

            // CoreV1Api 인스턴스 생성
            CoreV1Api api = new CoreV1Api();
            System.out.println(api.getApiClient().getSslCaCert());
            // 모든 네임스페이스의 Pod 목록을 가져옴
            CoreV1EventList a = api.listEventForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            System.out.println(a.getMetadata());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}