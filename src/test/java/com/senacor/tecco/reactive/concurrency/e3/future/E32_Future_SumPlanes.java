package com.senacor.tecco.reactive.concurrency.e3.future;

import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.Future;

/**
 * Retrieves plane information with futures
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E32_Future_SumPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsSummedUpWithFutures() throws Exception {

        //get Futures for articles
        Future<String> article777Future = fetchArticle("Boeing 777");
        Future<String> article747Future = fetchArticle("Boeing 747");

        //stop and wait for article
        String article777 = article777Future.get();
        String article747 = article747Future.get();

        //calculate build count
        int buildCountSum = parseBuildCountInt(article777) + parseBuildCountInt(article747);

        Summary.printCounter("777 and 747", buildCountSum);
    }

    // fetches an article from Wikipedia
    private Future<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleFuture(articleName);
    }

}
