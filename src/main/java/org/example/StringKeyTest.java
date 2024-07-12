package org.example;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import whatap.lang.value.MapValue;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringKeyTest {
    private String containerId;
    public String getContainerId() {
        return this.containerId;
    }

    private static final StringKeyLinkedMap<V1Pod> allPodByWatch = new StringKeyLinkedMap<V1Pod>();
    private static StringKeyLinkedMap<MapValue> podByNodeLookup = new StringKeyLinkedMap<MapValue>() {
        protected MapValue create(String nodeName){
            return new MapValue();
        }
    };

    // CoreV1Api 인스턴스 생성
    public static void main(String[] args) {
        try {
            // Kubernetes API 클라이언트 초기화
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            // CoreV1Api 인스턴스 생성
            CoreV1Api api = new CoreV1Api();
            V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            for (V1Pod pod : podList.getItems()){
                //System.out.println("FirstPodName="+ pod.getMetadata().getName());
                MapValue testMap = new MapValue();
                testMap.put("a","1");
                testMap.put("b","2");
                podByNodeLookup.intern(pod.getSpec().getNodeName());
//                StringKeyTest.allPodByWatch.put(pod.getMetadata().getUid(), pod);
            }
            for (String key : StringKeyTest.podByNodeLookup.keyArray()){
                System.out.println("key="+key+"//value="+podByNodeLookup.get(key));
            }
//            System.out.println("StringKeyTest.allPodByWatch.size()="+StringKeyTest.allPodByWatch.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

