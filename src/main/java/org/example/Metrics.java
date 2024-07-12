package org.example;
import io.kubernetes.client.openapi.*;
import io.kubernetes.client.util.Config;
import okhttp3.Call;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metrics {
    public static void main(String[] args) throws IOException, ApiException {
        //ApiClient 를 기본 경로 ~/.kube/config 를 통해 초기화
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        //header 값을 담을 맵
        Map<String, String> localVarHeaderParams = new HashMap();

        //http 요청 시 header 정보 중 accept 에 포함 시킬 내용

        //요청 보낼 때 사용할 다양한 쿼리 인자
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        //요청 보낼 때 사용할 쿠키값
        Map<String, String> localVarCookieParams = new HashMap();

        //어따쓰는건지 모르겠는데 일단 넣어야할 것 같아서 넣은 맵(?)
        Map<String, Object> localVarFormParams = new HashMap();

        Call get = client.buildCall("/metrics",
                "GET",
                localVarQueryParams,
                localVarCollectionQueryParams,
                null,
                localVarHeaderParams,
                localVarCookieParams,
                localVarFormParams,
                new String[]{"BearerToken"},
                null);

        ApiResponse<String> execute = client.execute(get, String.class);
        System.out.println(execute.getStatusCode());
        System.out.println(execute.getData());
    }
}
//process_cpu_seconds_total 36.63
//process_cpu_seconds_total 70.38
//