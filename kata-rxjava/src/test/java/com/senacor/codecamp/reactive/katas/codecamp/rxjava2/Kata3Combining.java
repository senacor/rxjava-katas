package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;
import static java.util.concurrent.TimeUnit.SECONDS;

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

        final String articleName = "Vaporwave";

        Observable<ParsedPage> pageObservable = wikiService.fetchArticleObservable("Vaporwave")
                .map(wikiService::parseMediaWikiText);
        Observable<Integer> ratings = pageObservable.map(ratingService::rate);
        Observable<Integer> wordCounts = pageObservable.map(countService::countWords);

        Observable.zip(ratings, wordCounts, (r, wc) -> String.format("{\"articleName\": %s, \"rating\": %s, \"wordCount\": %s}", articleName, r, wc))
                .doOnNext(System.out::println)
                .test()
                .awaitDone(1, SECONDS)
                .assertValueCount(1)
                .assertComplete();

    }

}
