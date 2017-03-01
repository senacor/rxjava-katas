package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = CountServiceImpl.create();
    private RatingService ratingService = RatingServiceImpl.create();

    /**
     * 1. fetch and parse a wiki article
     * 2. use {@link RatingService#rateFlux(ParsedPage)} and {@link CountService#countWordsFlux(ParsedPage)}.
     * Combine both information as JSON and print the JSON to the console.
     * Example {"articleName": "Superman", "rating": 3, "wordCount": 452}
     */
    @Test
    public void combiningObservable() throws Exception {
        wikiService.fetchArticleFlux("Bilbilis");
    }
}