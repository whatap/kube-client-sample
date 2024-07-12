package org.example;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1EnvVar;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1VolumeMount;
import io.kubernetes.client.util.Config;

import java.util.List;

public class ContainerVolumeExample {
    public static void main(String[] args) {
        try {
            // 기본 kubeconfig 사용
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);

            CoreV1Api api = new CoreV1Api();
            String namespace = "default"; // 네임스페이스 지정
            String podName = "java-apm-sample"; // Pod 이름 지정

            V1Pod pod = api.readNamespacedPod(podName, namespace, null);
            for (V1Container v1c :pod.getSpec().getContainers()){
                String name  = v1c.getName();
                if (name.equalsIgnoreCase("java-apm-sample")){
                    for (V1EnvVar v1EnvVar : v1c.getEnv()) {
                        System.out.println(v1EnvVar.getName());
                        System.out.println(v1EnvVar.getValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}