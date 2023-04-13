package dk.kb.factory;

import dk.kb.responseMessage.ResponseMessage;

import java.io.File;
import java.util.List;

public abstract class FileHandling implements FileHandlingInterface {

    @Override public <T> List<ResponseMessage> validateFile(String id, File file) {
        return null;
    }

    @Override public <T> List<ResponseMessage> validateFile(String id, File file, T arg3) {
        return null;
    }

    @Override public <T> List<ResponseMessage> validateFile(String id, File file, T arg3, T arg4) {
        return null;
    }

    @Override public <T> List<ResponseMessage> validateFile(String id, File file, T arg3, T arg4, T arg5) {
        return null;
    }

    @Override public <T> List<ResponseMessage> validateFile(String id, File file, T arg3, T arg4, T arg5, T arg6) {
        return null;
    }

}
