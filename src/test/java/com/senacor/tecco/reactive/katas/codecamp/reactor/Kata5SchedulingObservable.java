package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.services.WikiServiceImpl;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    /**
     * 1. use the {@link WikiServiceImpl#wikiArticleBeingReadFlux(long, TimeUnit)} to create a stream of
     * wiki article names being read
     * 2. take only the first 20 articles
     * 3. load and parse the article
     * 4. use the {@link RatingService#rateFlux(ParsedPage)} and {@link CountService#countWordsFlux(ParsedPage)}
     * to combine both as JSON and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
     * 5. measure the runtime
     * 6. add a scheduler to a specific position in the observable chain to reduce the execution time
     */
    @Test
    public void schedulingObservable() throws Exception {
        wikiService.wikiArticleBeingReadFlux(50, TimeUnit.MILLISECONDS);
    }
}