package dk.kb.veraPDFValidator;

import dk.kb.responseMessage.ResponseMessage;

import java.io.File;
import java.util.List;

public interface ValidatePDF {

    /**
     * This can validate a pdf file to se if it is a valid PDF.
     *
     * @param id The request id, could be anything
     * @param pdfFileToCheck The pdf file to validate
     * @param validateMode Different validation modes. For more info look at veraPDF
     * @param checkEmbeddedFiles If true, the validator will check for embedded files in the pds
     * @param checkPDF The pdf file will be validated to see if it is a valid pdf
     * @return Validated response message
     */
    List<ResponseMessage> validatePDF(String id, File pdfFileToCheck, String validateMode, boolean checkEmbeddedFiles, boolean checkPDF);

}
