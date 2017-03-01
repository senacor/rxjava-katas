package com.senacor.tecco.reactive.concurrency;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Created by mmenzel on 27.01.2016.
 */
public class PlaneArticleBaseTest {

    @Rule
    public final Watch watch = new Watch();

    public final WikiService wikiService = new WikiService("en");

    /**
     * extracts the number of planes built from an wikipedia article about a specific plane type
     * @param article article about plane type
     * @return number of planes built
     */
    public int parseBuildCountInt(String article) {
        String buildCount = ReactiveUtil.findValue(article, "number built");
        return Integer.parseInt(buildCount.replaceAll(",",""));
    }

    /**
     * extracts the number of planes built from an wikipedia article about a specific plane type
     * @param article article about plane type
     * @return number of planes built
     */
    public String parseBuildCount(String article){
        return ReactiveUtil.findValue(article, "number built");
    }

    /***
     * Converts a list with plane names to string
     * @param planes
     * @return
     */
    public String formatPlanes(String[] planes) {
        return Stream.of(planes).collect(joining(" and "));
    }

}
