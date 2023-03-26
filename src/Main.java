import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        String longUrl = "htt://google.com";
        if (Validators.checkIsBitlink(longUrl, Addresses.isBitlink))
            System.out.println("Общее количество переходов: "+ countClicks(longUrl));
        else if (Validators.checkIsLink(longUrl)) {
            System.out.println("Сокращенная ссылка " + shortUrl(longUrl));
        }
        else {
            System.out.println("Ссылка " + longUrl + " не является корректной");
        }
    }
    private static String countClicks(String inputUrl) throws IOException {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("unit", "month");
        payload.put("units", "-1");
        if (!Validators.checkIsBitlink(inputUrl, Addresses.isBitlink)) {
            return "not Bitlink!";
        }

        ResponseData response = BaseHttpClient.get(Addresses.summaryAddress, payload, inputUrl);
        JSONObject responseJson = response.getResponseJson();
        if (Validators.isStatusCodeOk(response.getStatusCode())) {
            return ""+responseJson.getJSONArray("link_clicks").getJSONObject(0).getInt("clicks");
        } else {
            responseJson.getString("description");
        }

        return "Exception occurred";
    }
    private static String shortUrl(String inputUrl) throws IOException {
        HashMap<String, String> body = new HashMap<>();
        body.put("long_url", inputUrl);
        if (!Validators.checkIsLink(inputUrl)) {
            return "not Link!";
        }

        ResponseData response = BaseHttpClient.post(Addresses.baseAddress, body, null, inputUrl);
        JSONObject responseJson = response.getResponseJson();
        if (Validators.isStatusCodeOk(response.getStatusCode())) {
            return responseJson.getString("id");
        } else {
            return "Exc";
        }

    }
}