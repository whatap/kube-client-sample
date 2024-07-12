package org.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;

import java.util.*;
import java.util.Map.Entry;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NodeStat {
    public String name;
    public long request_memory;
    public long request_cpu;
    public long alloctable_cpu;
    public long alloctable_memory;
    public long alloctable_pods;
    public  int pods;
    public long limit_cpu;
    public long limit_memory;
    private Map<String, String> labels;
    Map<String, Object> condition;
    public String getNodeStatus;
    public long kubectlDisplayedAge;
    public void putLabel(Map<String, String> labels) {
        this.labels = labels;
    }
}