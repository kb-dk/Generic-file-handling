package dk.kb.responseMessage;

import java.util.Map;

public interface ResponseMessageInterface {
    public String getId();
    public Map<String,String> getMessage();
    public Map<String, String> addMessage(String key,String message);
    public ResponseCode getResponseCode();
    public ResponseCode setResponseCode(ResponseCode responseCode);

    public String getTimeStamp();
}
