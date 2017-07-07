package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.advanced;
import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.hardcore;

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
        wikiService.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS)
        // 2. Filter the names so that only articles with at least 15 characters long names are accepted and print everything to the console
                .filter(s -> s.length() >= 15 )
                .take(3)
                .subscribe(System.out::println,
                        (error) -> System.err.println(error.getLocalizedMessage()),
                        () -> {
                            System.out.println("Completed");
                            waitMonitor.complete();
                        });

        waitMonitor.waitFor(20, TimeUnit.SECONDS);
    }

    @Test
    @KataClassification(hardcore)
    public void filterObservable2() throws Exception {

        WaitMonitor waitMonitor = new WaitMonitor();

        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
        // 2. The stream delivers to many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)
                .sample(500, TimeUnit.MILLISECONDS)
                .take(4)
                .subscribe(System.out::println,
                        (e) -> System.err.println(e.getLocalizedMessage()),
                        () -> {
                            System.out.println("Completed");
                            waitMonitor.complete();
                        });

        waitMonitor.waitFor(5, TimeUnit.SECONDS);
    }
}
