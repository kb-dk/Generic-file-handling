package dk.kb.responseMessage;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseMessage implements ResponseMessageInterface {
    private final String id;
    private final Map<String, String> message;
    private ResponseCode responseCode;
    private final String timeStamp;

    private String artifactId = "N/A";
    private String versionNo = "N/A";
    private String validatorVersionNo = "N/A";
    private String validatorName = "N/A";


    public ResponseMessage(String id) {
        this.id = id;
        this.message = new HashMap<>();
        this.timeStamp = LocalTime.now().toString();
    }

    @Override public String getId() {
        return this.id;
    }

    @Override public Map<String, String> getMessage() {
        return this.message;
    }

    @Override public Map<String, String> addMessage(String key, String message) {
        this.message.put(key, message);
        return this.message;
    }

    @Override public ResponseCode getResponseCode() {
        return this.responseCode;
    }

    @Override public ResponseCode setResponseCode(ResponseCode responseCode) {
        if (this.responseCode == null) {
            return this.responseCode = responseCode;
        }
        return this.responseCode;
    }

    @Override
    public String getArtifactId() {
        return this.artifactId;
    }

    @Override
    public String getVersionNo() {
        return this.versionNo;
    }

    @Override public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public void setValidatorVersionNo(String validatorVersionNo) {
        this.validatorVersionNo = validatorVersionNo;
    }

    public void setValidatorName(String validatorName) {
        this.validatorName = validatorName;
    }

    @Override
    public String getValidatorInfo() {
        return this.validatorName + " " + this.validatorVersionNo;
    }

}
