import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BaseHttpClient {

    public static JSONObject responseToJson(HttpEntity entity) throws IOException {
        if (entity != null) {
            InputStream inputStream = entity.getContent();
            String responseBody = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return new JSONObject(responseBody);
        }
        return null;
    }

    private static StringEntity buildJsonBody(HashMap<String, String> bodyContent) {
        StringBuilder body = new StringBuilder("{");
        for (Map.Entry<String, String> map : bodyContent.entrySet()) {
            body.append("\"").append(map.getKey()).append("\"").append(":").append("\"").append(map.getValue()).append("\"");
        }
        body.append("}");

        return new StringEntity(String.valueOf(body), ContentType.APPLICATION_JSON);
    }
    private static String buildQueryParams(HashMap<String, String> bodyContent) {
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> map : bodyContent.entrySet()) {
            body.append("?").append(map.getKey()).append("=").append(map.getValue()).append("&");
        }

        return body.toString();
    }
    public static ResponseData post(
            String address,
            HashMap<String, String> body,
            HashMap<String, String> payload,
            String inputUrl) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            if (payload != null) {
                String queryParams = buildQueryParams(payload);
                address += queryParams;
            }
            if (inputUrl != null) {
                address = address.replace("%url%", inputUrl);
            }

            HttpPost request  = new HttpPost(address);
            if (body != null) {
                request.setEntity(buildJsonBody(body));
            }
            request.setHeader("Authorization", Config.bitlyToken);
            CloseableHttpResponse response = httpClient.execute(request);
            JSONObject responseJson = responseToJson(response.getEntity());
            return new ResponseData(response.getStatusLine().getStatusCode(), responseJson);
        }
    }
    public static ResponseData get(
            String address,
            HashMap<String, String> payload,
            String inputUrl) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            if (payload != null) {
                String queryParams = buildQueryParams(payload);
                address += queryParams;
            }
            if (inputUrl != null) {
                address = address.replace("%url%", inputUrl);
            }
            HttpGet request = new HttpGet(address);

            request.setHeader("Authorization", Config.bitlyToken);
            CloseableHttpResponse response = httpClient.execute(request);
            JSONObject responseJson = responseToJson(response.getEntity());
            return new ResponseData(
                    response.getStatusLine().getStatusCode(), responseJson);
        }
    }
}