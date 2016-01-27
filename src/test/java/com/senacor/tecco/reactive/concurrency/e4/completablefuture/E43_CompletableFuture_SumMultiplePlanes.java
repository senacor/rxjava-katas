package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class E43_CompletableFuture_SumMultiplePlanes {

    private final WikiService wikiService = new WikiService("en");

    @Rule
    public final Watch watch = new Watch();


    @Test
    public void thatPlaneBuildCountIsSummedUpWithCompletableFuture() throws Exception {

        String[] planes = {"Boeing 777","Boeing 747"};

        CompletableFuture<Integer>[] futures = new CompletableFuture[planes.length];

        for(int i= 0; i < planes.length; i++) {
            futures[i] = fetchArticle(planes[i])
                    .thenApply(this::parseBuildCount);
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

