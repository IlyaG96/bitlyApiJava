import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class BitlyMethods {
    static String countClicks(String inputUrl) throws IOException {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("unit", "month");
        payload.put("units", "-1");

        ResponseData response = BaseHttpClient.get(Addresses.summaryAddress, payload, inputUrl);
        if (!response.hasResponseJson()) {
            throw new IOException();
        }
        JSONObject responseJson = response.getResponseJson();
        return ""+responseJson.getJSONArray("link_clicks").getJSONObject(0).getInt("clicks");
    }
    static String shortUrl(String inputUrl) throws IOException {
        HashMap<String, String> body = new HashMap<>();
        body.put("long_url", inputUrl);
        ResponseData response = BaseHttpClient.post(Addresses.baseAddress, body, null, inputUrl);

        if (!response.hasResponseJson()) {
            throw new IOException();
        }
        JSONObject responseJson = response.getResponseJson();
        return responseJson.getString("id");
    }
}
