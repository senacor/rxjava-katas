package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.observables.JoinObservable;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();
    private RatingService ratingService = new RatingService();

    @Test
    public void combiningObservable() throws Exception {
        String article = "Boeing 777";
        // 1. fetch and parse Wikiarticle
        wikiService.fetchArticleObservable(article)
                .flatMap(wikiService::parseMediaWikiTextObservable)
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}
                .flatMap( x -> {
                    Observable<Integer> ratings = ratingService.rateObservable(x);
                    Observable<Integer> counts = countService.countWordsObervable(x);
                    return JoinObservable.when(
                            JoinObservable.from(ratings)
                                    .and(counts)
                                    .then((rate, count) -> "articleName: " + x.getName() + ", rating: " + rate + ", wordCount:" + count)
                    ).toObservable();
                })
                .subscribe(
                        x -> System.out.println(x),
                        Throwable::printStackTrace,
                        () -> System.out.println("done")
                );

    }

}
