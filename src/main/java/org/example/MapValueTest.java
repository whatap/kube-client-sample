package org.example;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1NodeStatus;
import io.kubernetes.client.openapi.models.V1NodeSystemInfo;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import whatap.lang.value.MapValue;
import whatap.util.StringUtil;

import java.io.IOException;

public class MapValueTest {
    public static void main(String[] args) throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        // CoreV1Api 인스턴스 생성
        CoreV1Api api = new CoreV1Api();
        V1Node v1Node = api.readNode("minikube", null);
        V1NodeStatus v1NodeStatus = v1Node.getStatus();
        if (v1NodeStatus != null && v1NodeStatus.getNodeInfo() != null){
            V1NodeSystemInfo nodeInfo = v1NodeStatus.getNodeInfo();
            if (nodeInfo.getContainerRuntimeVersion() != null){
                String containerRuntimeVersion = nodeInfo.getContainerRuntimeVersion();
                System.out.println(containerRuntimeVersion);
                String[] containerRuntimeToken = StringUtil.split(containerRuntimeVersion, "://");
                System.out.println(containerRuntimeToken.length);
                if (containerRuntimeToken.length == 2){
                    System.out.println(containerRuntimeToken[0]);
                }
            }
        }
    }
}