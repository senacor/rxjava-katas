package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import org.junit.Test;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.advanced;
import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.hardcore;
import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.fail;

/**
 * @author Andreas Keefer
 */
public class Kata4Filtering {

    private final WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(advanced)
    public void filterObservable() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. Filter the names so that only articles with at least 15 characters long names are accepted and print everything to the console
        wikiService.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS)
                .filter(a -> a.length() >= 15)
                .take(1)
                .subscribe(System.out::println, (error) -> {error.printStackTrace();}, waitMonitor::complete);
        waitMonitor.waitFor(20, SECONDS);
    }

    @Test
    @KataClassification(hardcore)
    public void filterObservable2() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. The stream delivers too many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)
        wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .sample(500, MILLISECONDS)
                .subscribe(System.out::println, (error) -> {error.printStackTrace();}, waitMonitor::complete);
        waitMonitor.waitFor(20, SECONDS);

    }
}
