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
    private static String setQuery(HashMap<String, String> payload, String address) {
        if (payload != null) {
            String queryParams = buildQueryParams(payload);
            address += queryParams;
        } return address;
    }
    private static String replaceUrl(String inputUrl, String address) {
        if (inputUrl != null) {
            address = address.replace("%url%", inputUrl);
        }
        return address;
    }

    private static HttpGet createRequest(String address,
                                         HashMap<String, String> payload,
                                         String inputUrl) {

        address = setQuery(payload, replaceUrl(inputUrl, address));
        HttpGet request  = new HttpGet(address);
        request.setHeader("Authorization", Config.bitlyToken);
        return request;

    }
    private static HttpPost createRequest(String address,
                                             HashMap<String, String> body,
                                             HashMap<String, String> payload,
                                             String inputUrl) {

        address = setQuery(payload, replaceUrl(inputUrl, address));
        HttpPost request  = new HttpPost(address);
        if (body != null) {
            request.setEntity(buildJsonBody(body));
        }
        request.setHeader("Authorization", Config.bitlyToken);

        return request;
    }

    public static ResponseData post(
            String address,
            HashMap<String, String> body,
            HashMap<String, String> payload,
            String inputUrl) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = createRequest(address, body, payload, inputUrl);
            CloseableHttpResponse response = httpClient.execute(request);
            if (!Validators.isStatusCodeOk(response.getStatusLine().getStatusCode())) {
                throw new IOException();
            }
            JSONObject responseJson = responseToJson(response.getEntity());

            return new ResponseData(response.getStatusLine().getStatusCode(), responseJson);
        }
        catch (IOException exc) {
            return new ResponseData(0, null);
        }
    }
    public static ResponseData get(
            String address,
            HashMap<String, String> payload,
            String inputUrl) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = createRequest(address, payload, inputUrl);
            CloseableHttpResponse response = httpClient.execute(request);

            if (!Validators.isStatusCodeOk(response.getStatusLine().getStatusCode())) {
                throw new IOException();
            }
            JSONObject responseJson = responseToJson(response.getEntity());

            return new ResponseData(
                    response.getStatusLine().getStatusCode(), responseJson);
        }
        catch (IOException exc) {
            return new ResponseData(0, null);
        }
    }
}