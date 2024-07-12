package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.VersionApi;
import io.kubernetes.client.openapi.models.VersionInfo;
import io.kubernetes.client.util.Config;

import java.io.IOException;

public class SwitchTest {
    public static void main(String[] args) throws IOException, ApiException {
        String day = "mon";
        switch (day){
            case "test":{
                System.out.println("test");
            }
                break;

            case "mon":{
                System.out.println("mon");
            }
                System.out.println("monmon");
                break;

            case "tue":{
                System.out.println("tue");
            }
                break;
        }
            System.out.println("haha");
    }
}
