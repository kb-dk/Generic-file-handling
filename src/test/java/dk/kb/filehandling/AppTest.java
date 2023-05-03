package dk.kb.filehandling;

import dk.kb.veraPDFValidator.VeraPDFValidator;
import dk.kb.responseMessage.ResponseMessage;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws URISyntaxException {
        File pdfFile = new File(AppTest.class.getClassLoader().getResource("20210101_aarhusstiftstidende_section01_page001_ast20210101x11#0001.pdf").toURI().getPath());
        VeraPDFValidator validator = new VeraPDFValidator();
        List<ResponseMessage> rm = validator.validatePDF("test", pdfFile, "2b", true, true);

        assertFalse(rm.isEmpty());
    }
    @Test
    public void testForEmbeddedFileInPDF() throws URISyntaxException {
        File pdfFile = new File(AppTest.class.getClassLoader().getResource("20221201_information_section01_page004_inf20221201x11#0004.pdf").toURI().getPath());
        VeraPDFValidator validator = new VeraPDFValidator();
        List<ResponseMessage> rm = validator.validatePDF("test", pdfFile, "2b", true, true);

        assertFalse(rm.isEmpty());
    }
}
