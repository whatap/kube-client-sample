package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeAddress;
import io.kubernetes.client.openapi.models.V1NodeCondition;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.util.Config;
import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NodeMain_DateTime {
    public static void main(String[] args) throws IOException, ApiException{
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1NodeList Nodelist = api.listNode(null, null, null, null, null, null, null, null, null, null);

        // 현재 시간 구하기
        NodeStat ns;

        String nodeName;


        for (V1Node a : Nodelist.getItems()) {

            //노드 이름 가져오기
//            System.out.println(a.getMetadata().toString());
            System.out.println(a.getMetadata().getLabels());
            ns = new NodeStat();

            nodeName = a.getMetadata().getName();
//            System.out.println(a.getMetadata().get);


            ns.name = nodeName;
            String nodeStatus;

            for (V1NodeCondition condition : a.getStatus().getConditions()) {
                Map<String, Object> anodeCondition = new HashMap<>();
//                String conditionType = condition.getType().toString();
//                System.out.println("Condition : " + condition.toString());
//                ns.condition.put(condition.getType().toString(), condition.getStatus());

//                System.out.println("Condition Type: " + condition.getType());
//                System.out.println("Condition Status: " + nodeConditions.toString());
            }
            if (a.getSpec() == null){
                continue;
            }
            if (a.getStatus() == null || a.getStatus().getAddresses() == null){
                continue;
            }

            for (V1NodeAddress nodeAddressObj : a.getStatus().getAddresses()) {
                String type = nodeAddressObj.getType();
                String address = nodeAddressObj.getAddress();
                System.out.println("type="+type+"//address="+address);
            }
//            ns.condition.get("Ready");
//            nodeUnSchedulable = a.getSpec().getUnschedulable();
//            if(nodeUnSchedulable){
//                ns.status =
//                nodeUnSchedulable.
//            }

//            ns.condition = nodeConditions;


            //노드 나이 출력
            DateTime t = new DateTime(a.getMetadata().getCreationTimestamp().toEpochSecond()*1000);

            long ageInMillis = DateTime.now().getMillis() - t.getMillis();
            long ageInMinutes = ageInMillis / (60*1000);
            long ageInHours = ageInMinutes / 60;
            long ageInDays = ageInHours / 24;

            System.out.println("Node Age (days):" + ageInHours);
            System.out.println("Node Age (days):" + ageInDays);
//            Date nodeCreationTime = null;
//            try {
//                nodeCreationTime = dateFormat.parse(nodeCreationTimestamp);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//            long ageInMillis = currentTime.getTime() - nodeCreationTime.getTime();
//            long ageInMinutes = ageInMillis / (60 * 1000);
//            long ageInHours = ageInMinutes / 60;
//            long ageInDays = ageInHours / 24;
//            ns.age = ageInMillis;

//            System.out.println("Node Age (days): " + ageInDays);
//            System.out.println("Condition Status: " + ns.);
        }
    }
}