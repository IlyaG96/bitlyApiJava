import org.json.JSONObject;

public class ResponseData {
    private final int statusCode;
    private final JSONObject responseJson;
    public ResponseData(int code, JSONObject entity) {
        this.statusCode = code;
        this.responseJson = entity;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public JSONObject getResponseJson() {
        return responseJson;
    }
}