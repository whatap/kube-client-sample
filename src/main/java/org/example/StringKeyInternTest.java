package org.example;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import whatap.util.StringKeyLinkedMap;

import java.util.ArrayList;
import java.util.List;

public class StringKeyInternTest {
    private String containerId;
    public String getContainerId() {
        return this.containerId;
    }

    private static final StringKeyLinkedMap<V1Pod> allPodByWatch = new StringKeyLinkedMap<V1Pod>();

    private static final StringKeyLinkedMap<List<V1PodSimple>> podCache = new StringKeyLinkedMap<List<V1PodSimple>>() {
        protected List<V1PodSimple> create(String nodeName){
            return new ArrayList<V1PodSimple>();
        }
    };

    private static StringKeyLinkedMap<StringKeyLinkedMap<V1PodSimple>> podByNodeLookup = new StringKeyLinkedMap<StringKeyLinkedMap<V1PodSimple>>() {
        protected StringKeyLinkedMap<V1PodSimple> create(String nodeName){
            return new StringKeyLinkedMap<V1PodSimple>();
        }
    };

    private static StringKeyLinkedMap<V1PodSimple> v1PodSimpleStringKeyLinkedMap = new StringKeyLinkedMap<V1PodSimple>(){
        protected V1PodSimple create(V1Pod v1Pod){
            return new V1PodSimple(v1Pod);
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
            String searchUid = "";
            for (V1Pod pod : podList.getItems()){
                //System.out.println("FirstPodName="+ pod.getMetadata().getName());

                StringKeyInternTest.allPodByWatch.put(pod.getMetadata().getUid(), pod);
                StringKeyInternTest.allPodByWatch.put(pod.getMetadata().getUid(), null);
                if (pod.getSpec() != null && pod.getSpec().getNodeName() != null) {
                    StringKeyInternTest.podCache.intern(pod.getSpec().getNodeName()).add(new V1PodSimple(pod));
                    v1PodSimpleStringKeyLinkedMap = podByNodeLookup.intern(pod.getSpec().getNodeName());
                    v1PodSimpleStringKeyLinkedMap.intern(pod.getSpec().getNodeName());
                    System.out.println("v1PodSimpleStringKeyLinkedMap="+v1PodSimpleStringKeyLinkedMap);
                    System.out.println(StringKeyInternTest.podCache.intern(pod.getSpec().getNodeName()));
                }
                searchUid = pod.getMetadata().getName();
            }
//            System.out.println(StringKeyInternTest.allPodByWatch.intern(searchUid));
            System.out.println("StringKeyTest.allPodByWatch.size()="+ StringKeyInternTest.allPodByWatch.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

