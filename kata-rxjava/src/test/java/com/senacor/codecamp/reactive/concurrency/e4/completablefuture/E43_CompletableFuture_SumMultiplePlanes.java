package com.senacor.codecamp.reactive.concurrency.e4.completablefuture;

import com.senacor.codecamp.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.codecamp.reactive.concurrency.Summary;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Retrieves and combine plane information with CompletableFutures
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E43_CompletableFuture_SumMultiplePlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsSummedUpWithCompletableFuture() throws Exception {

        String[] planes = {"Boeing 777", "Boeing 747"};

        //create List of CompletableFutures for build count
        List<CompletableFuture<Integer>> futures = new ArrayList<>(planes.length);

        for (String plane : planes) {
            futures.add(fetchArticle(plane)
                    .thenApply(this::parseBuildCountInt));
        }

        //wait for fulfillment of all futures
        int sumBuildCount = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply((v) -> {
                    //collect all results and sum up
                    int sum = 0;
                    for (CompletableFuture<Integer> future : futures) {
                        sum += future.join();
                    }
                    return (sum);
                })
                .get();

        Summary.printCounter(formatPlanes(planes), sumBuildCount);
    }

    // fetches an article from Wikipedia
    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }
}
