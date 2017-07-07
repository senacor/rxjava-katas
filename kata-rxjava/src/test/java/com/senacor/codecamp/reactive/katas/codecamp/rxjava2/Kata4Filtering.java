package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;

import io.reactivex.disposables.Disposable;

import com.senacor.codecamp.reactive.katas.KataClassification;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.advanced;
import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.hardcore;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;


/**
 * @author Andreas Keefer
 */
public class Kata4Filtering {

    private final WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(advanced)
    public void filterObservable() throws Exception {
    	final WaitMonitor monitor = new WaitMonitor();
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. Filter the names so that only articles with at least 15 characters long names are accepted and print everything to the console

        Disposable sub = wikiService.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS)
        .filter((name) -> name.length() >= 15)
        .subscribe((next) -> System.out.println(next),
        		Throwable::printStackTrace,
        		() -> {
        			print("Complete!");
        			monitor.complete();
        		});
        
        monitor.waitFor(10, TimeUnit.SECONDS);
        sub.dispose();
    }

    @Test
    @KataClassification(hardcore)
    public void filterObservable2() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. The stream delivers to many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)

        wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS);
    }
}
