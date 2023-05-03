package test.veraPDFValidator;

import dk.kb.responseMessage.ResponseCode;
import dk.kb.responseMessage.ResponseMessage;
import dk.kb.veraPDFValidator.VeraPDFValidator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VeraPDFValidatorTest {

    @Test
    public void canHandleNoValidPDFFile_Error() {
        VeraPDFValidator veraPDFValidator = new VeraPDFValidator();

        List<ResponseMessage> validationMgs = veraPDFValidator.validatePDF("ABCD", new File("Unknow file.pdf"), "2A", true, true);

        assertEquals(1, validationMgs.size());
        assertEquals("ABCD", validationMgs.get(0).getId());
        assertEquals(ResponseCode.FAILED, validationMgs.get(0).getResponseCode());
        assertTrue(validationMgs.get(0).getMessage().containsKey("Unknown Error"));
        assertEquals("error msg:Couldn't parse stream", validationMgs.get(0).getMessage().get("Unknown Error"));
    }


    @Test
    public void canEmbeddedPDFFile_Ok() {
        VeraPDFValidator veraPDFValidator = new VeraPDFValidator();

        Path resourceDirectory = Paths.get("src", "main", "java", "test", "resources");
        String filePathToTest = resourceDirectory.toAbsolutePath() + "/" + "avisEmbeddedValideringOk.pdf";

        List<ResponseMessage> validationMgs = veraPDFValidator.validatePDF("ABCD", new File(filePathToTest), "2A", true, false);

        assertEquals(1, validationMgs.size());
        assertEquals("ABCD", validationMgs.get(0).getId());
        assertEquals(ResponseCode.SUCCESS, validationMgs.get(0).getResponseCode());
        assertEquals(0, validationMgs.get(0).getMessage().size());
    }


    @Test
    public void canValidPDFFile_Ok() {
        VeraPDFValidator veraPDFValidator = new VeraPDFValidator();

        Path resourceDirectory = Paths.get("src", "main", "java", "test", "resources");
        String filePathToTest = resourceDirectory.toAbsolutePath() + "/" + "veraPDF test suite 6-7-4-t01-pass-b.pdf"; // File must not be renamed, then it will not work

        List<ResponseMessage> validationMgs = veraPDFValidator.validatePDF("ABCD", new File(filePathToTest), "2A", false, true);

        assertEquals(1, validationMgs.size());
        assertEquals("ABCD", validationMgs.get(0).getId());
        assertEquals(ResponseCode.SUCCESS, validationMgs.get(0).getResponseCode());
        assertEquals(0, validationMgs.get(0).getMessage().size());
    }

}
