package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ContainerStatus;
import io.kubernetes.client.openapi.models.V1OwnerReference;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class PodContainerImage {
    public static void main(String[] args) throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1PodList PodList = api.listPodForAllNamespaces(null, null, null, null, 100, null, null, null, null,null);

        // 현재 시간 구하기
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date currentTime = new Date();

        for (V1Pod a : PodList.getItems()) {
//            System.out.println("Pod name :" + a.getMetadata().getName() + "// owner:" + a.getMetadata().getOwnerReferences());
            if (null != a.getSpec())
                {
                    for(V1Container container:  a.getSpec().getContainers()) {
                        System.out.println("Pod name :" + a.getMetadata().getName() + "// containerName:" + container.getName());
                    }
                }
            if (null != a.getStatus()){
                for(V1ContainerStatus containerStatus: a.getStatus().getContainerStatuses()){
                    System.out.println("containerId :" + containerStatus.getContainerID()+"// containerName:"+containerStatus.getName());
                }
            }
            else{
                System.out.println("Pod name :" + a.getMetadata().getName());
            }

//            for (V1ContainerStatus ConStatus: a.getStatus().getContainerStatuses()){
//                V1ContainerState Constate = ConStatus.getState();
//                System.out.println("ConStatus:"+ Constate.getRunning());
//                System.out.println("ConStatus:"+ Constate.getTerminated());
//                System.out.println("ConStatus:"+ Constate.getWaiting());
//            }
//            for (V1PodCondition podCon: a.getStatus().getConditions()){
//                System.out.println("Pod IP :"+ podCon.toString());
//            }
//            System.out.println("Pod IP :"+ a.getStatus().getPodIP());
//            long currentTimeMillis = currentTime.getTime();
//            long creationTimeMillis = a.getMetadata().getCreationTimestamp().toEpochSecond()*1000;
//            long ageInMillis = currentTimeMillis - creationTimeMillis;

//            System.out.println(ageInMillis/(60*1000));

            int restartCount = 0 ;
//            for (V1ContainerStatus containerStatus: a.getStatus().getContainerStatuses()){
//                if (a.getMetadata().getName().toString().equals("multi-con-pod")){
//                    System.out.println(containerStatus);
//                }
//                System.out.println(a.getMetadata().getName()+"//"+"containerStatus="+containerStatus.getRestartCount());
//                restartCount += containerStatus.getRestartCount();
//                System.out.println("Pod Restarts :"+ restartCount);
//                restartCount+=restartCount;

//            }

//            System.out.println("Node metadata :"+ a.getMetadata());

//            System.out.println("Node cpu :"+ a.getMetadata().getName().toString());
//            노드 이름 가져오기
//            System.out.println(a.getStatus());
//
//            ns = new NodeStat();
//
//            nodeName = a.getMetadata(c).getName();
//            ns.name = nodeName;
//            String nodeStatus;



//            파드 나이 출력
//            String podCreationTimestamp = a.getMetadata().getCreationTimestamp().toString();
//            Date podCreationTime = null;
//            try {
//                podCreationTime = dateFormat.parse(podCreationTimestamp);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//            long ageInMillis = currentTime.getTime() - podCreationTime.getTime();
//            long ageInMinutes = ageInMillis / (60 * 1000);
//            long ageInHours = ageInMinutes / 60;
//            long ageInDays = ageInHours / 24;
//
//            System.out.println("POD Age (hours): " + ageInHours);
//            System.out.println("POD Age (days): " + ageInDays);
//            System.out.println("POD Age (millis): " + ageInMillis);
        }
    }
}
