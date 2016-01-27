package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class E43_CompletableFuture_SumMultiplePlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsSummedUpWithCompletableFuture() throws Exception {

        String[] planes = {"Boeing 777","Boeing 747"};

        CompletableFuture<Integer>[] futures = new CompletableFuture[planes.length];

        for(int i= 0; i < planes.length; i++) {
            futures[i] = fetchArticle(planes[i])
                    .thenApply(this::parseBuildCountInt);
        }

        int sumBuildCount = CompletableFuture.allOf(futures)
            .thenApply((v) -> {
                int sum = 0;
                for(CompletableFuture<Integer> future: futures){
                    sum += future.join();
                }
                return(sum);
            })
            .get();

        Summary.printCounter(formatPlanes(planes), sumBuildCount);
    }

    // fetches an article from Wikipedia
    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }
}

