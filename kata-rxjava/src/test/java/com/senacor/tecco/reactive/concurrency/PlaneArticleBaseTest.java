package com.senacor.tecco.reactive.concurrency;

import com.senacor.tecco.reactive.services.PlainInfoService;
import com.senacor.tecco.reactive.util.ReactiveUtil;
import com.senacor.tecco.reactive.util.Watch;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Created by mmenzel on 27.01.2016.
 */
public class PlaneArticleBaseTest {

    @Rule
    public final Watch watch = new Watch();

    public final WikiService wikiService = WikiService.create("en");
    protected final PlainInfoService plainInfoService = new PlainInfoService();

    /**
     * extracts the number of planes built from an wikipedia article about a specific plane type
     *
     * @param article article about plane type
     * @return number of planes built
     */
    public int parseBuildCountInt(String article) {
        return plainInfoService.parseBuildCountInt(article);
    }

    /**
     * extracts the number of planes built from an wikipedia article about a specific plane type
     *
     * @param article article about plane type
     * @return number of planes built
     */
    public String parseBuildCount(String article) {
        return plainInfoService.parseBuildCount(article);
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
