package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Andreas Keefer
 */
public class Kata5Scheduling {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();
    
    WaitMonitor waitMonitor = new WaitMonitor();

    @Test
    @KataClassification(KataClassification.Classification.mandatory)
    public void schedulingObservable() throws Exception {
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        // 2. take only the first 20 articles
        // 3. load and parse the article
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
        // 5. measure the runtime
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
        	.take(20)
        	.flatMap(name -> wikiService.fetchArticleObservable(name).subscribeOn(Schedulers.io()))
        	// subscribeOn: der Scheduler bem Erzeuger kann verändert werden (hier: wikiService...) 
        	// observeOn: macht einfach weiter auf neuem Thread, aber gefetcht wurde schon davor 
        	// meistens subscribeOn sinnvoll, observeOn eigentlich nur bei GUI sachen... 
        	.flatMap(article -> wikiService.parseMediaWikiTextObservable(article))
        	.flatMap(parsedPage -> {
         		Observable<Integer> r = ratingService.rateObservable(parsedPage); 
         		Observable<Integer> c = countService.countWordsObservable(parsedPage); 
         		return Observable.zip(r, c, (rating, count) -> "{rating: " + rating.toString() + ", wordCount: " + count.toString() + "}").subscribeOn(Schedulers.computation());  
        	})
//        	.doOnNext(s -> System.out.println(s)); 
        .subscribe(s -> System.out.println(s), Throwable::printStackTrace, () -> waitMonitor.complete());
        
        waitMonitor.waitFor(15, TimeUnit.SECONDS);
        
    }

}
