package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


import io.reactivex.Observable;
import io.reactivex.Scheduler;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata5Scheduling {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    @Test
    @KataClassification(KataClassification.Classification.mandatory)
    public void schedulingObservable() throws Exception {
    	WaitMonitor monitor = new WaitMonitor();
    	
    	Scheduler s = ReactiveUtil.newRxScheduler(5, "sss");
    	
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
    	Disposable sub = wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
        // 2. take only the first 20 articles
		.take(20)
        // 3. load and parse the article
		.flatMap((name) -> wikiService.fetchArticleObservable(name).subscribeOn(Schedulers.io()))
		.flatMap(wikiService::parseMediaWikiTextObservable)
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
		.flatMap((page) -> {			
			return Observable.zip(
					ratingService.rateObservable(page).subscribeOn(s),
					countService.countWordsObservable(page).subscribeOn(s),
					(rating, count) -> String.format("{ rating: %s, wordCount: %s }", rating, count)
					);
		})
        // 5. measure the runtime
		.subscribe(next -> System.out.println(next),
				Throwable::printStackTrace,
				() -> {
					System.out.println("completed!");
					monitor.complete();
				});
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time
    	
    	monitor.waitFor(10, TimeUnit.SECONDS);
    	sub.dispose();
    }

}
