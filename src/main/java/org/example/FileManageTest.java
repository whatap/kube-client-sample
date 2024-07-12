package org.example;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.ApiResponse;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FileManageTest {
    public static void main(String[] args) throws IOException, ApiException, JSONException {
        File f = new File("test.log");
        BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
        FileTime createdTime = attr.creationTime();

        FileTime lastModifiedTime = attr.lastModifiedTime();
        System.out.println("createdTime="+createdTime);
        System.out.println("lastModifiedTime="+lastModifiedTime);
        System.out.println("attr.fileKey()="+attr.fileKey());
        }
    }
//}
//fileTime=2023-10-23T05:59:24Z
//fileTime=2023-10-23T06:01:40Z