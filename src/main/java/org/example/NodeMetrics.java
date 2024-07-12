package org.example;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

import java.io.IOException;

public class NodeMetrics {
    public static void main(String[] args) throws IOException {
        // 쿠버네티스 API 서버에서 메트릭 가져오기
        ApiClient client = Config.defaultClient();
        Counter.build().name("kubernetes_pods_total").help("Total number of pods").register();
    }
}