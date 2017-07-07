package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

/**
 * @author Andreas Keefer
 */
public class Kata3Combining {

    private WikiService wikiService = WikiService.create();
    private CountService countService = CountService.create();
    private RatingService ratingService = RatingService.create();

    @Test
    @KataClassification(mandatory)
    public void combiningObservable() throws Exception {
        // 1. fetch and parse Wikiarticle
        wikiService.fetchArticleObservable("Hamburger")
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> {
                    Observable<Integer> ratedObservable = ratingService.rateObservable(parsedPage);
                    Observable<Integer> countedObservable = countService.countWordsObservable(parsedPage);
                    String name = "Hamburger";
                    return Observable.zip(ratedObservable, countedObservable, (ro, co) ->
                    String.format("\"articleName\":\"%s\", \"rating\": \"%s\", \"wordCount\": \"%s\"",
                            name, ro, co));
                })

                .subscribe(
                        next -> System.out.print(next)

                );
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        // wikiService.fetchArticleObservable()
    }

}
