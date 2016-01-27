package com.senacor.tecco.reactive.concurrency.e3.future;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Test;

import java.util.concurrent.Future;

public class E31_Future_CountPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsFetchedWithFutures() throws Exception {

        //get Futures for articles
        Future<String> article777Future = fetchArticle("Boeing 777");
        Future<String> article747Future = fetchArticle("Boeing 747");

        //stop and wait for article
        String article777 = article777Future.get();
        String article747 = article747Future.get();

        //calculate build count
        String buildCount777 = parseBuildCount(article777);
        String buildCount747 = parseBuildCount(article747);

        Summary.printCounter("777", buildCount777);
        Summary.printCounter("747", buildCount747);
    }

    // fetches an article from Wikipedia
    private Future<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleFuture(articleName);
    }

}
