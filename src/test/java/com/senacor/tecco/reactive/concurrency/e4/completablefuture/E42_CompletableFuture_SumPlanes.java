package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class E42_CompletableFuture_SumPlanes {

    private final WikiService wikiService = new WikiService("en");

    @Rule
    public final Watch watch = new Watch();


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

    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }


    private String parseBuildCount(String article) {
        return ReactiveUtil.findValue(article, "number built");
    }

}

