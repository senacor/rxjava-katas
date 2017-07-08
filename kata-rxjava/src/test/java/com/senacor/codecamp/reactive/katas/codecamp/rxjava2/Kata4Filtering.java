package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.advanced;
import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.hardcore;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;

/**
 * @author Andreas Keefer
 */
public class Kata4Filtering {

    private final WikiService wikiService = WikiService.create();
    WaitMonitor waitMonitor = new WaitMonitor();

    @Test
    @KataClassification(advanced)
    public void filterObservable() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. Filter the names so that only articles with at least 15 characters long names are accepted and print everything to the console

//        wikiService.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS)
//        	.filter(article -> article.length() >= 15)
//        	.doOnNext(s -> System.out.println(s));
////        .subscribe(s -> System.out.println(s)); 
//        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

    @Test
    @KataClassification(hardcore)
    public void filterObservable2() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. The stream delivers to many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)
    	
        wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
        	.sample(500, TimeUnit.MILLISECONDS)
        	.doOnNext(s -> System.out.println(s)); 
        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }
}
