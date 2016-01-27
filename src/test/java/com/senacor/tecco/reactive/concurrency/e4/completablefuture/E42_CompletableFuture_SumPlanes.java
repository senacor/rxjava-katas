package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * Retrieves and combine plane information with CompletableFutures
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E42_CompletableFuture_SumPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsSummedUpWithCompletableFuture() throws Exception {

        //fetch article on 777 and parse build count
        CompletableFuture<Integer> buildCount777Future = fetchArticle("Boeing 777")
                .thenApply(this::parseBuildCountInt);

        //fetch article on 747 and parse build count
        CompletableFuture<Integer> buildCount747Future = fetchArticle("Boeing 747")
                .thenApply(this::parseBuildCountInt);

        //combine both futures
        buildCount777Future
                .thenCombine(buildCount747Future, (buildCount777, buildCount747) -> {
                    //extract number of built planes and calculate sum
                    int buildCountSum = buildCount777 + buildCount747;
                    Summary.printCounter("777 and 747", buildCountSum);
                    return buildCountSum;
                })
                .get();
    }

    // fetches an article from Wikipedia
    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }

}

