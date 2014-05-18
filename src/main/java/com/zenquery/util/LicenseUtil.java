package com.zenquery.util;

import com.license4j.License;
import com.license4j.LicenseValidator;
import com.license4j.ValidationStatus;
import com.zenquery.exception.LicenseInvalidException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by willy on 16.05.14.
 */
public class LicenseUtil {
    private static final Logger logger = Logger.getLogger(LicenseUtil.class);

    private Properties licenseProperties;

    private Resource licenseFile;

    private Resource publicKeyFile;

    public void setLicenseProperties(Properties licenseProperties) {
        this.licenseProperties = licenseProperties;
    }

    public void setLicenseFile(Resource licenseFile) {
        this.licenseFile = licenseFile;
    }

    public void setPublicKeyFile(Resource publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public LicenseUtil() {

    }

    public void initialize() throws LicenseInvalidException {
        validateLicense();
    }

    public void validateLicense() throws LicenseInvalidException {
        String licenseKey;
        String publicKey;
        try {
            licenseKey = IOUtils.toString(licenseFile.getInputStream(), "UTF-8");
            publicKey = IOUtils.toString(publicKeyFile.getInputStream(), "UTF-8");
        } catch (IOException e) {
            throw new LicenseInvalidException();
        }

        License license = LicenseValidator.validate(
                licenseKey,
                publicKey,
                licenseProperties.getProperty("zenQuery.productId"),
                null,
                null,
                null,
                null
        );

        if (!license.getValidationStatus().equals(ValidationStatus.LICENSE_VALID)) {
            throw new LicenseInvalidException();
        }
    }
}
