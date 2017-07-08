package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

import org.junit.Test;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;

import io.reactivex.Observable;

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
         	.flatMap(parsedPage -> {
         		Observable<Integer> r = ratingService.rateObservable(parsedPage); 
         		Observable<Integer> c = countService.countWordsObservable(parsedPage); 
         		return Observable.zip(r, c, (rating, count) -> rating.toString() + ", " + count.toString());  
         	})
         .subscribe(s -> System.out.println(s)); 	
    }

}
