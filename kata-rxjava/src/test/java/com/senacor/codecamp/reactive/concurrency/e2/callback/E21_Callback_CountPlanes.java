package com.senacor.codecamp.reactive.concurrency.e2.callback;

import com.senacor.codecamp.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.codecamp.reactive.concurrency.Summary;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Retrieves plane information with callbacks
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E21_Callback_CountPlanes extends PlaneArticleBaseTest {

    // error handler function
    Consumer<Exception> exceptionConsumer = (e) -> {
        e.printStackTrace();
    };

    @Test
    public void thatPlaneBuildCountIsFetchedWithCallback() throws Exception {
        WaitMonitor monitor = new WaitMonitor(2);

        //get article on 777
        fetchArticle("Boeing 777", (article777) -> {

            //extract and print number of built planes
            String buildCount777 = parseBuildCount(article777);
            Summary.printCounter("777", buildCount777);

            monitor.complete();
        }, exceptionConsumer);

        //get article on 747
        fetchArticle("Boeing 747", (article747) -> {

            //extract and print number of built planes
            String buildCount747 = parseBuildCount(article747);
            Summary.printCounter("747", buildCount747);

            monitor.complete();
        }, exceptionConsumer);

        monitor.waitFor(3000, TimeUnit.MILLISECONDS);
    }

    // fetches an article from Wikipedia
    void fetchArticle(String articleName, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer) {
        wikiService.fetchArticleCallback(articleName, articleConsumer, exceptionConsumer);
    }

}
