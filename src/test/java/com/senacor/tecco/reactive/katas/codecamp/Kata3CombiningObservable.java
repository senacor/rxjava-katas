package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
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
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        // wikiService.fetchArticleObservable()

        // ConnectableObservabe .publish -> alle weiteren aus Variable subscriben, dann .connect
        // -> ruft nur einmal auf, sonst mehrfacher Aufruf!

        final String articleName = "Flugzeug";
        wikiService.fetchArticleObservable(articleName)
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> Observable.zip(ratingService.rateObservable(parsedPage),
                        countService.countWordsObervable(parsedPage),
                        (r, c) -> new String("{name:" + articleName + ",rating:" + r + ",count:" + c + "}")
                ))
                .subscribe(x -> System.out.println(x));
    }
}
