package com.senacor.tecco.reactive.concurrency.e2.callback;

import com.senacor.tecco.reactive.WaitMonitor;
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
public class E22_Callback_SumPlanes extends PlaneArticleBaseTest{

    // error handler function
    Consumer<Exception> exceptionConsumer = (e)->{e.printStackTrace();};

    @Test
    public void thatPlaneBuildCountIsSummedUpWithCallback() throws Exception {
        WaitMonitor monitor = new WaitMonitor();

        //fetch articles from wikipedia
        fetchArticle("Boeing 777", (article777) -> {
            fetchArticle("Boeing 747", (article747) -> {

                //extract number of built planes and calculate sum
                int buildCountSum = parseBuildCountInt(article777) + parseBuildCountInt(article747);
                Summary.printCounter("777 and 747", buildCountSum);

                monitor.complete();
            }, exceptionConsumer);
        }, exceptionConsumer);

        monitor.waitFor(3000,TimeUnit.MILLISECONDS);
    }

    // fetches an article from Wikipedia
    void fetchArticle(String articleName, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer) {
        wikiService.fetchArticleCallback(articleName, articleConsumer, exceptionConsumer);
    }


}
