package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Retrieves and combine plane information with CompletableFutures and Streams
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E44_CompletableFuture_SumMultiplePlanesWithStreams extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsSummedUpWithCompletableFutureAndStreams() throws Exception {

        String[] planes = {"Boeing 777", "Boeing 747"};

        //create List of CompletableFutures for build count
        List<CompletableFuture<Integer>> futures = Arrays.stream(planes)
                //fetch article future for plane and chain future with build count parser
                .map(plane -> fetchArticle(plane)
                        .thenApply(this::parseBuildCountInt))
                //collect all build counts
                .collect(Collectors.toList());

        CompletableFuture<Integer> sumBuildCount = futures.stream()
                .reduce((a, b) -> a.thenCombine(b, Integer::sum))
                .get();

        sumBuildCount.thenAccept(sum -> Summary.printCounter(formatPlanes(planes), sum)).get(30, SECONDS);
    }

    // fetches an article from Wikipedia
    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }
}

