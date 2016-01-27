package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class E42_CompletableFuture_SumPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsSummedUpWithCompletableFuture() throws Exception {

        CompletableFuture<String> buildCount777Future = fetchArticle("Boeing 777")
                .thenApply(this::parseBuildCount);

        CompletableFuture<String> buildCount747Future = fetchArticle("Boeing 747")
                .thenApply(this::parseBuildCount);

        String[] planesBuilt = buildCount777Future
                .thenCombine(buildCount747Future, (buildCount777, buildCount747) -> {
                    return new String[]{"777 and 747", buildCount777 + buildCount747};
                })
                .get();

        Summary.printCounter(planesBuilt);
    }

    // fetches an article from Wikipedia
    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }

}

