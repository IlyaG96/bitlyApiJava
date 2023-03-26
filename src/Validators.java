import java.io.IOException;

public class Validators {

    public static boolean checkIsLink(String inputUrl) {
        return inputUrl.startsWith("http");

    }
    public static boolean checkIsBitlink(String inputUrl, String address) throws IOException {
        ResponseData response = BaseHttpClient.get(address, null, inputUrl);
        return isStatusCodeOk(response.getStatusCode());
    }
    public static boolean isStatusCodeOk(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
}
