package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

/**
 * @author Andreas Keefer
 */
public class Kata3Combining {

    private WikiService wikiService = WikiService.create();
    private CountService countService = CountService.create();
    private RatingService ratingService = RatingService.create();
    
    private final String articleName = "Observable";

    @Test
    @KataClassification(mandatory)
    public void combiningObservable() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and countService.countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

	Observable<ParsedPage> articleObservable = wikiService.fetchArticleObservable(articleName).map(wikiService::parseMediaWikiText);
    	
	Observable.zip(
			articleObservable.flatMap(ratingService::rateObservable),
			articleObservable.flatMap(countService::countWordsObservable),
			(rating, count) -> {
				return String.format("{ articleName: %s, rating: %s, wordCount: %s", articleName, rating, count);
			})
	.subscribe(System.out::println);
    	
    	
        // wikiService.fetchArticleObservable()
    }

}
