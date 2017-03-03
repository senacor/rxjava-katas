package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.ReactiveUtil;

/**
 * @author Andreas Keefer
 */
public class PlainInfoService {

    /**
     * extracts the number of planes built from an wikipedia article about a specific plane type
     *
     * @param article article about plane type
     * @return number of planes built
     */
    public String parseBuildCount(String article) {
        return ReactiveUtil.findValue(article, "number built");
    }

    /**
     * extracts the number of planes built from an wikipedia article about a specific plane type
     *
     * @param article article about plane type
     * @return number of planes built
     */
    public int parseBuildCountInt(String article) {
        String buildCount = parseBuildCount(article);
        return Integer.parseInt(buildCount.replaceAll(",", ""));
    }
}
