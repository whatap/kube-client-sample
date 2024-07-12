package org.example;
import org.example.NodeStat;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeCondition;
import io.kubernetes.client.openapi.models.V1NodeStatus;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1NodeSpec;

import io.kubernetes.client.util.Config;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class NodeMain {
    public static void main(String[] args) throws IOException, ApiException{
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1NodeList Nodelist = api.listNode(null, null, null, null, null, null, null, null, null, null);
        // 현재 시간 구하기
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date currentTime = new Date();
        NodeStat ns;

        String nodeName;


        ArrayList<Map<String, Object>> nodeConditions = new ArrayList<>();
        for (V1Node a : Nodelist.getItems()) {

            System.out.println("Node name :"+ a.getStatus().getConditions().toString());
//            System.out.println("Node metadata :"+ a.getMetadata());

//            System.out.println("Node cpu :"+ a.getMetadata().getName().toString());
            //노드 이름 가져오기
//            System.out.println(a.getStatus());

            ns = new NodeStat();

            nodeName = a.getMetadata().getName();
            ns.name = nodeName;
            String nodeStatus;



            //노드 나이 출력
            String nodeCreationTimestamp = a.getMetadata().getCreationTimestamp().toString();
            Date nodeCreationTime = null;
            try {
                nodeCreationTime = dateFormat.parse(nodeCreationTimestamp);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            long ageInMillis = currentTime.getTime() - nodeCreationTime.getTime();
            long ageInMinutes = ageInMillis / (60 * 1000);
            long ageInHours = ageInMinutes / 60;
            long ageInDays = ageInHours / 24;

            System.out.println("Node Age (hours): " + ageInHours);
            System.out.println("Node Age (days): " + ageInDays);
            System.out.println("Condition Status: " + ageInMillis);
        }
    }
}