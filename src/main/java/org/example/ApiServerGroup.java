package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.ApisApi;
import io.kubernetes.client.openapi.apis.VersionApi;
import io.kubernetes.client.openapi.models.V1APIGroup;
import io.kubernetes.client.openapi.models.V1APIGroupList;
import io.kubernetes.client.openapi.models.VersionInfo;
import io.kubernetes.client.util.Config;

import java.io.IOException;

public class ApiServerGroup {
    public static void main(String[] args) throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        ApisApi api = new ApisApi(client);
        V1APIGroupList resp = api.getAPIVersions();
        for (V1APIGroup item: resp.getGroups()) {
            System.out.println("clientVersion:" + item.getVersions());
        }
    }
}