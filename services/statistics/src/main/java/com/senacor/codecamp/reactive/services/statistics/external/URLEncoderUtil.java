package com.senacor.codecamp.reactive.services.statistics.external;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Andri Bremm
 */
public class URLEncoderUtil {

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
