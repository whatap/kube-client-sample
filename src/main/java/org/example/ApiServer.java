package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.ApisApi;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.VersionApi;
import io.kubernetes.client.openapi.models.V1APIGroupList;
import io.kubernetes.client.openapi.models.VersionInfo;
import io.kubernetes.client.util.Config;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiServer {
    public static void main(String[] args) throws IOException, ApiException {
        VersionApi versionApi = new VersionApi();
        VersionInfo versionInfo = versionApi.getCode();
        System.out.println("getBuildDate:"+versionInfo.getBuildDate());
        System.out.println("getGitVersion:"+versionInfo.getGitVersion());
        System.out.println("getGoVersion:"+versionInfo.getGoVersion());
        System.out.println("getGoVersion:"+versionInfo.getPlatform());
        System.out.println("versionInfo:"+versionInfo.toString());
    }
}