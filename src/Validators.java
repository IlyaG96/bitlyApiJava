import java.net.MalformedURLException;
import java.net.URL;

public class Validators {
    public static boolean checkIsLink(String inputUrl){
        try {
            URL url = new URL(inputUrl);
            return url != null;
        } catch (MalformedURLException exception)  {
            return false;
        }
    }
    public static boolean checkIsBitlink(String inputUrl, String address){
        ResponseData response = BaseHttpClient.get(address, null, inputUrl);
        return isStatusCodeOk(response.getStatusCode());
    }
    public static boolean isStatusCodeOk(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
}
