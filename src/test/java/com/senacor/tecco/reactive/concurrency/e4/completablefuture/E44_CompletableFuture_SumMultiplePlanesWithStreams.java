package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class E44_CompletableFuture_SumMultiplePlanesWithStreams {

    private final WikiService wikiService = new WikiService("en");

    @Rule
    public final Watch watch = new Watch();


    @Test
    public void thatPlaneBuildCountIsSummedUpWithCompletableFutureAndStreams() throws Exception {

        String[] planes = {"Boeing 777","Boeing 747"};

        //create List of CompletableFutures for build count
        List<CompletableFuture<Integer>> futures = Arrays.stream(planes)
                //fetch article future for plane and chain future with build count parser
                .map(plane -> fetchArticle(plane)
                    .thenApply(this::parseBuildCount))
                //collect all build counts
                .collect(Collectors.<CompletableFuture<Integer>>toList());

        //wait for fulfillment of all futures
        int sumBuildCount = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
            .thenApply((v) -> {
                return futures.stream().
                    //map future to result
                    map(future -> future.join()).
                    //sum up
                    reduce(0, Integer::sum);
            })
            .get();

        Summary.printCounter(formatPlanes(planes), sumBuildCount);
    }

    private String formatPlanes(String[] planes) {
        String result = planes[0];

        for(int i= 1; i < planes.length; i++) {
            result += " and " + planes[i];
        }

        return result;
    }

    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }

    private int parseBuildCount(String article) {
        String buildCount = ReactiveUtil.findValue(article, "number built");
        return Integer.parseInt(buildCount.replaceAll(",",""));
    }

}

