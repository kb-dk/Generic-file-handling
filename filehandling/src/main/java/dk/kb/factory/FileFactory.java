package dk.kb.factory;

import dk.kb.factory.veraPDFValidator.VeraPDFValidator;

public class FileFactory {
    public static FileHandling getFileHandling(String fileHandlingType){
        if(fileHandlingType == null){
            return null;
        }
        if (fileHandlingType.equalsIgnoreCase("VERAPDF")){
            return new VeraPDFValidator();
        }
        return null;
    }

}
