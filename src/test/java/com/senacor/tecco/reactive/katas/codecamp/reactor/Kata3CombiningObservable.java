package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();
    private RatingService ratingService = new RatingService();

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