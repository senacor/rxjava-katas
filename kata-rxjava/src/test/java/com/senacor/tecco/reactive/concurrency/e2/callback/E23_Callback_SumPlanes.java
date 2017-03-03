package com.senacor.tecco.reactive.concurrency.e2.callback;

import com.senacor.tecco.reactive.util.WaitMonitor;
import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Retrieves and combines plane information with callbacks
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E23_Callback_SumPlanes extends PlaneArticleBaseTest {

    // error handler function
    Consumer<Exception> exceptionConsumer = (e) -> {
        e.printStackTrace();
    };

    @Test
    public void thatPlaneBuildCountIsSummedUpWithCallback() throws Exception {
        WaitMonitor monitor = new WaitMonitor();

        LinkedBlockingQueue<String> article777Queue = new LinkedBlockingQueue<>();

        fetchArticle("Boeing 777", (article777) -> {
            try {
                article777Queue.put(article777);
            } catch (InterruptedException e) {
                exceptionConsumer.accept(e);
            }
        }, exceptionConsumer);

        fetchArticle("Boeing 747", (article747) -> {

            //retrieve 777 Article
            String article777 = null;
            try {
                article777 = article777Queue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                exceptionConsumer.accept(e);
            }

            //extract number of built planes and calculate sum
            int buildCountSum = parseBuildCountInt(article777) + parseBuildCountInt(article747);
            Summary.printCounter("777 and 747", buildCountSum);

            monitor.complete();
        }, exceptionConsumer);

        monitor.waitFor(3000, TimeUnit.MILLISECONDS);
    }

    // fetches an article from Wikipedia
    void fetchArticle(String articleName, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer) {
        wikiService.fetchArticleCallback(articleName, articleConsumer, exceptionConsumer);
    }

}
