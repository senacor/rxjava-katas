package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import io.vertx.core.json.Json;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();
    private RatingService ratingService = new RatingService();

    @Test
    public void combiningObservable() throws Exception {
        String articleName = "Flugzeug";

        wikiService.fetchArticleObservable(articleName)
                .map(wikiService::parseMediaWikiText)
                .flatMap(parsedPage ->
                        Observable.zip(ratingService.rateObservable(parsedPage),
                            countService.countWordsObervable(parsedPage),
                            (rating, counts) -> "{\"aricleName\": \"" + articleName + "\", \"rating\": " + rating + ", \"wordCount\": " + counts + "}"))
                .subscribe(System.out::println);
    }

}
