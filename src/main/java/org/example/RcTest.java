package org.example;
import com.google.gson.Gson;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1ReplicationController;
import io.kubernetes.client.openapi.models.V1ReplicationControllerList;
import io.kubernetes.client.util.Config;

import java.io.IOException;

public class RcTest {
    public static void main(String[] args) throws IOException, ApiException {
            // Kubernetes API 클라이언트 초기화
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);

            // CoreV1Api 인스턴스 생성
            CoreV1Api api = new CoreV1Api();

                V1ReplicationControllerList r = api.listReplicationControllerForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
                for (V1ReplicationController d : r.getItems()){
                    // JSON 결과 출력
                    System.out.println(d.getMetadata());
            }
    }
}