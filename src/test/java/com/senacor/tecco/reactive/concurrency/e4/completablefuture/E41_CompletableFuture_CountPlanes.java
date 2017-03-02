package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * Retrieves plane information with CompletableFutures
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E41_CompletableFuture_CountPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneInfoIsCombinedWithCompletableFuture() throws Exception {

        CompletableFuture<Void> buildCount777Future = fetchArticle("Boeing 777")
                .thenApply(article -> parseBuildCount(article))
                .thenAccept(buildCount -> Summary.printCounter("777", buildCount));

        CompletableFuture<Void> buildCount747Future = fetchArticle("Boeing 747")
                .thenApply(this::parseBuildCount)
                .thenAccept(buildCount -> Summary.printCounter("747", buildCount));

        buildCount777Future.get();
        buildCount747Future.get();
    }

    // fetches an article from Wikipedia
    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }

}

