package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();
    private RatingService ratingService = new RatingService();

    @Test
    public void combiningObservable() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}


        wikiService.fetchArticleObservable("Boeing 777")
            .map(article -> wikiService.parseMediaWikiText(article))
            .flatMap(text -> {

                Observable<Integer> rating = ratingService.rateObservable(text);
                Observable<Integer> counter = countService.countWordsObervable(text);

                return Observable.zip(counter, rating, (s1, s2) -> new JsonObject().put("articleName", text.getPageId()).put("rating", s1).put("wordCount", s2));
            })
            .subscribe(
                    n -> System.out.println("JSON: " + n.encodePrettily()),
                    err -> System.err.println("Error: " + err.getMessage()),
                    () -> System.out.println("Done")
            );
    }

}
