package com.zenquery.util;

import org.apache.log4j.Logger;

import java.security.MessageDigest;

/**
 * Created by willy on 14.04.14.
 */
public class StringUtil {
    private static final Logger logger = Logger.getLogger(StringUtil.class);

    public static String hashWithSha256(String input) {
        String output = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(input.getBytes("UTF-8"));
            output = new String(messageDigest.digest());
        } catch (Exception e) {
            logger.debug(e);
        }

        return output;
    }
}
