package com.senacor.tecco.reactive.katas.codecamp.rxjava2;

import com.senacor.tecco.reactive.services.*;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = CountServiceImpl.create();
    private RatingService ratingService = RatingServiceImpl.create();

    @Test
    public void combiningObservable() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        // wikiService.fetchArticleObservable()
    }

}
