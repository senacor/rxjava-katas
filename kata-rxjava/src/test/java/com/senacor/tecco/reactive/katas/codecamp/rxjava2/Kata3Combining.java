package com.senacor.tecco.reactive.katas.codecamp.rxjava2;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata3Combining {

    private WikiService wikiService = WikiService.create();
    private CountService countService = CountService.create();
    private RatingService ratingService = RatingService.create();

    @Test
    public void combiningObservable() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        // wikiService.fetchArticleObservable()
    }

}
