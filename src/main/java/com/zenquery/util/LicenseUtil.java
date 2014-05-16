package com.zenquery.util;

import com.license4j.License;
import com.license4j.LicenseValidator;

/**
 * Created by willy on 16.05.14.
 */
public class LicenseUtil {
    public LicenseUtil() {

    }

    public Boolean validate() {
        License license = LicenseValidator.validate("", "", "", "", "", 0);

        return license != null;
    }
}
