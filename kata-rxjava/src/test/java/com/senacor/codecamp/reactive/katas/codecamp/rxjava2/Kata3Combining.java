package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
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
        Observable<ParsedPage> parsedPage = wikiService.fetchArticleObservable("Schnitzel")
                .map(wikiService::parseMediaWikiText);

        Observable<Integer> rating = parsedPage.flatMap(ratingService::rateObservable);

        Observable<Integer> wordCount = parsedPage.flatMap(countService::countWordsObservable);

        Observable<String> name = Observable.just("Schnitzel");

        Observable<String> result = Observable.zip(rating, wordCount, name, (s1, s2, s3) -> "{\"articleName\": \"" + s3 + "\", \"rating\": " + s1 + "\", \"wordCount\": " + s2 + "}");

        result.subscribe(System.out::print);
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        // wikiService.fetchArticleObservable()
    }

}
