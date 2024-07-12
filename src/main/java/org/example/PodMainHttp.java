package org.example;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.ApiResponse;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import okhttp3.Call;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class PodMainHttp {
    public static void main(String[] args) throws IOException, ApiException, JSONException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        String _continue = null;
        CoreV1Api api = new CoreV1Api();
        Call call = api.listPodForAllNamespacesCall(
                null,
                _continue,
                null,
                null,
                100,
                null,
                null,
                null,
                null,
                null,
                null);
        ApiResponse<byte[]> resp = api.getApiClient().execute(call, new TypeToken<byte[]>() {}.getType());
        String json = new String(resp.getData());
        JSONObject myObject = new JSONObject(json);
        System.out.println(json);
        System.out.println(myObject);
        }
    }
//}
