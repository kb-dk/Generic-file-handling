package dk.kb.factory;

import dk.kb.responseMessage.ResponseMessage;

import java.io.File;
import java.util.List;

public interface FileHandlingInterface {

    public <T> List<ResponseMessage> validateFile(String id, File file);
    public <T> List<ResponseMessage> validateFile(String id, File file, T arg3);
    public <T> List<ResponseMessage> validateFile(String id, File file, T arg3, T arg4);
    public <T> List<ResponseMessage> validateFile(String id, File file, T arg3, T arg4, T arg5);
    public <T> List<ResponseMessage> validateFile(String id, File file, T arg3, T arg4, T arg5, T arg6);
//    public List<ResponseMessage> getResponseMessages();
//    public List<ResponseMessage> setResponseMessages(List<ResponseMessage> responseMessages);

}
