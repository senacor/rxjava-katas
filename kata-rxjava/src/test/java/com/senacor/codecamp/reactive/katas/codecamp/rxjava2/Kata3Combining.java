package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.internal.operators.observable.ObservableReduceMaybe;
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
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}
        wikiService.fetchArticleObservable("Superman")
                .map(wikiService::parseMediaWikiText)
                .flatMap((ParsedPage parsedPage) -> {
                    Observable<Integer> obsRating = Observable.fromCallable(() -> ratingService.rate(parsedPage));
                    Observable<Integer> obsWordCount = Observable.fromCallable(() -> countService.countWords(parsedPage));

                    return obsRating.zipWith(obsWordCount, (rating, wordCount) ->
                                String.format("{\"articleName\": \"Superman\", \"rating\": %d, \"wordCount\": %d}",
                                        rating, wordCount));
                })
                .forEach(System.out::println);

        // wikiService.fetchArticleObservable()
    }

}
