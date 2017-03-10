package com.senacor.codecamp.reactive.katas.codecamp.reactor;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata5Scheduling {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    /**
     * 1. use the {@link WikiService#wikiArticleBeingReadFlux(long, TimeUnit)} to create a stream of
     * wiki article names being read
     * 2. take only the first 20 articles
     * 3. load and parse the article
     * 4. use the {@link RatingService#rateFlux(ParsedPage)} and {@link CountService#countWordsFlux(ParsedPage)}
     * to combine both as JSON and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
     * 5. measure the runtime
     * 6. add a scheduler to a specific position in the chain to reduce the execution time
     */
    @Test
    @KataClassification(KataClassification.Classification.mandatory)
    public void scheduling() throws Exception {
        wikiService.wikiArticleBeingReadFlux(50, TimeUnit.MILLISECONDS);
    }
}