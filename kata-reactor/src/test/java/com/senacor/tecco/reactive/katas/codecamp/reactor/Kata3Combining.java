package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.katas.KataClassification;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import static com.senacor.tecco.reactive.katas.KataClassification.Classification.mandatory;

/**
 * @author Andreas Keefer
 */
public class Kata3Combining {

    private WikiService wikiService = WikiService.create();
    private CountService countService = CountService.create();
    private RatingService ratingService = RatingService.create();

    /**
     * 1. fetch and parse a wiki article
     * 2. use {@link RatingService#rateFlux(ParsedPage)} and {@link CountService#countWordsFlux(ParsedPage)}.
     * Combine both information as JSON and print the JSON to the console.
     * Example {"articleName": "Superman", "rating": 3, "wordCount": 452}
     */
    @Test
    @KataClassification(mandatory)
    public void combining() throws Exception {
        wikiService.fetchArticleFlux("Bilbilis");
    }
}