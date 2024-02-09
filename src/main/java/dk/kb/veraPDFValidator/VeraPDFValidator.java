package dk.kb.veraPDFValidator;

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
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

public class VeraPDFValidator implements ValidatePDF {

    private String veraPDFValidatorName = "N/A";
    private String veraPDFValidatorVersionNo = "N/A";

    public VeraPDFValidator() {
    }

    @Override
    public List<ResponseMessage> validatePDF(String id, File pdfFileToCheck, String validateMode, boolean checkEmbeddedFiles, boolean checkPDF) {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        GFModelParser parser = null;

        try {
            PDFAFlavour pdfAFlavour = PDFAFlavour.byFlavourId(validateMode);
            parser = GFModelParser.createModelWithFlavour(pdfFileToCheck, pdfAFlavour);
            if (checkPDF) {
                responseMessages.add(compliantToValidationModeCheck(parser, pdfAFlavour, id));
            }
            if (checkEmbeddedFiles) {
                responseMessages.add(embeddedPDFFilesCheck(parser, id));
            }

        } catch (VeraPDFException e) {
            ResponseMessage responseMessage = new ResponseMessage(id);
            responseMessage.setResponseCode(ResponseCode.FAILED);
            responseMessage.addMessage("Unknown Error", "error msg:" + e.getMessage());
            responseMessage.setArtifactId(getProjectArtifactId());
            responseMessage.setVersionNo(getProjectVersionNo());
            responseMessages.add(responseMessage);
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

        responseMessage.setValidatorName(veraPDFValidatorName);
        responseMessage.setValidatorVersionNo(veraPDFValidatorVersionNo);
        responseMessage.setArtifactId(getProjectArtifactId());
        responseMessage.setVersionNo(getProjectVersionNo());

        return responseMessage;
    }

    private ResponseMessage compliantToValidationModeCheck(GFModelParser parser, PDFAFlavour pdfAFlavour, String id) throws VeraPDFException {
        ResponseMessage responseMessage = new ResponseMessage(id);
        final ValidationResult validationResult = isValidPDF(parser, pdfAFlavour);
        if (validationResult.isCompliant()) {
            responseMessage.setResponseCode(ResponseCode.SUCCESS);
        } else {
            responseMessage.setResponseCode(ResponseCode.ERROR);
            for (TestAssertion assertion : validationResult.getTestAssertions()) {
                responseMessage.addMessage(assertion.getRuleId().getClause(), assertion.getMessage());
            }
        }

        responseMessage.setValidatorName(veraPDFValidatorName);
        responseMessage.setValidatorVersionNo(veraPDFValidatorVersionNo);
        responseMessage.setArtifactId(getProjectArtifactId());
        responseMessage.setVersionNo(getProjectVersionNo());

        return responseMessage;
    }


    private String getProjectVersionNo() {
        final Properties properties = loadProperties();
        String projectVersionNumber = properties.getProperty("version");

        return validateArtifactOrVersionNo(projectVersionNumber);
    }


    protected ValidationResult isValidPDF(final GFModelParser parser, final PDFAFlavour flavour) throws VeraPDFException {
        final PDFAValidator validator = ValidatorFactory.createValidator(flavour, false, 1);

        veraPDFValidatorName = validator.getDetails().getName();
        veraPDFValidatorVersionNo = validator.getDetails().getVersion();

        return validator.validate(parser);
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


    private String getProjectArtifactId() {
        final Properties properties = loadProperties();
        String projectArtifactId = properties.getProperty("artifactId");

        return validateArtifactOrVersionNo(projectArtifactId);
    }

    private Properties loadProperties() {
        final Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("projekt.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    private String validateArtifactOrVersionNo(String value) {
        String validated = "";

        if ((value == null) || (value.isEmpty())) {
            validated = "N/A";
        } else {
            validated = value;
        }

        return validated;
    }

}
