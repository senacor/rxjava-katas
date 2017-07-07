package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
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
        final String articleName = "Observable";
        Observable<ParsedPage> wikiText = wikiService.fetchArticleObservable(articleName)
                .map(v -> wikiService.parseMediaWikiText(v));

        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}
        Observable<String> count = wikiText.flatMap(countService::countWordsObservable)
                .map(i -> String.format("\"wordCount\": %s", i));
        Observable<String> rating = wikiText.flatMap(ratingService::rateObservable)
                .map(i -> String.format("\"rating\": %s", i));

        // Placeholder for something like ParsedArticle::getTitle
        Observable<String> title = Observable.just(articleName)
                .map(s -> String.format("\"articleName\": \"%s\"", s));

        Observable.zip(title, rating, count, (t, r, c) -> String.format("{%s, %s, %s}", t, r, c))
                .subscribe(System.out::println);
    }
}
