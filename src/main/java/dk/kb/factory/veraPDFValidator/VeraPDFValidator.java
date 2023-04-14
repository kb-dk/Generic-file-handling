package dk.kb.factory.veraPDFValidator;

import dk.kb.factory.FileHandling;
import dk.kb.responseMessage.ResponseCode;
import dk.kb.responseMessage.ResponseMessage;
import org.verapdf.core.VeraPDFException;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.gf.model.GFModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.RuleId;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class VeraPDFValidator extends FileHandling{

    private List<ResponseMessage> responseMessages;
    public VeraPDFValidator(){
        this.responseMessages = new ArrayList<>();
    }
    @Override public <T> List<ResponseMessage> validateFile(String id, File file, T veraPDFflavour, T validateEmbedded, T validateCompliant) {
        return validatePDF(id,file,(String)veraPDFflavour,(Boolean)validateEmbedded,(Boolean)validateCompliant);
    }


    public List<ResponseMessage> validatePDF(String id, File pdfFileToCheck, String validateMode, boolean checkEmbeddedFiles, boolean checkPDF) {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        GFModelParser parser = null;

        try {

            PDFAFlavour pdfAFlavour = PDFAFlavour.byFlavourId(validateMode);
            parser = GFModelParser.createModelWithFlavour(pdfFileToCheck, pdfAFlavour);
            if (checkPDF){
                responseMessages.add(compliantToValidationModeCheck(parser, pdfAFlavour, id));
            }
            if(checkEmbeddedFiles){
                responseMessages.add(embeddedPDFFilesCheck(parser, id));
            }

        } catch (VeraPDFException e) {
            ResponseMessage responseMessage = new ResponseMessage(id);
            responseMessage.setResponseCode(ResponseCode.FAILED);
            responseMessage.addMessage("Unknown Error", "error msg:" + e.getMessage());
        } finally {
            if (parser != null) {
                parser.close();
            }
        }

        return responseMessages;
    }

    private ResponseMessage embeddedPDFFilesCheck(GFModelParser parser, String id) {
        ResponseMessage responseMessage = new ResponseMessage(id);
        final List<FeatureTreeNode> pdfEmbeddedFiles = checkEmbeddedFiles(parser);

        if (pdfEmbeddedFiles.isEmpty()) {
            responseMessage.setResponseCode(ResponseCode.SUCCESS);
        } else {
            responseMessage.setResponseCode(ResponseCode.ERROR);
            for (FeatureTreeNode embedded : pdfEmbeddedFiles) {
                for (FeatureTreeNode metaData : embedded.getChildren()) {
                    responseMessage.addMessage(metaData.getName(), metaData.getName() + " " + metaData.getValue());
                }
            }
        }
        return responseMessage;
    }

    private ResponseMessage compliantToValidationModeCheck(GFModelParser parser, PDFAFlavour pdfAFlavour, String id) throws VeraPDFException {
        ResponseMessage responseMessage = new ResponseMessage(id);
        final ValidationResult validationResult = isValidPDF(parser, pdfAFlavour);
        if (validationResult.isCompliant()) {
            responseMessage.setResponseCode(ResponseCode.SUCCESS);
        } else {
            responseMessage.setResponseCode(ResponseCode.ERROR);
            for (TestAssertion assertion:validationResult.getTestAssertions()) {
                responseMessage.addMessage(assertion.getRuleId().getClause(),assertion.getMessage());
            }

        }
        return responseMessage;
    }

    protected ValidationResult isValidPDF(final GFModelParser parser, final PDFAFlavour flavour) throws VeraPDFException {
        final PDFAValidator validator = ValidatorFactory.createValidator(flavour, false, 1);
        final ValidationResult result = validator.validate(parser);
        return result;
    }

    protected List<FeatureTreeNode> checkEmbeddedFiles(GFModelParser parser) {
        final FeatureExtractorConfig featureExtractorConfig = new FeatureExtractorConfig() {
            @Override
            public boolean isFeatureEnabled(FeatureObjectType type) {
                return type != null;
            }

            @Override
            public boolean isAnyFeatureEnabled(EnumSet<FeatureObjectType> types) {
                return !types.isEmpty();
            }

            @Override
            public EnumSet<FeatureObjectType> getEnabledFeatures() {
                return null;
            }
        };
        return parser.getFeatures(featureExtractorConfig).getFeatureTreesForType(FeatureObjectType.EMBEDDED_FILE);
    }

}
