package com.senacor.tecco.reactive.concurrency.e4.completablefuture;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class E41_CompletableFuture_CountPlanes {

    private final WikiService wikiService = new WikiService("en");

    @Rule
    public final Watch watch = new Watch();


    @Test
    public void thatPlaneInfoIsCombinedWithCompletableFuture() throws Exception {

        CompletableFuture<String> buildCount777Future = fetchArticle("Boeing 777")
                .thenApply(this::parseBuildCount);

        CompletableFuture<String> buildCount747Future = fetchArticle("Boeing 747")
                .thenApply(this::parseBuildCount);

        String[][] planesBuilt = buildCount777Future
                .thenCombine(buildCount747Future, (buildCount777, buildCount747) -> {
                    return new String[][]{{"777", buildCount777}, {"747", buildCount747}};
                })
                .get();

        Summary.printCounter(planesBuilt[0]);
        Summary.printCounter(planesBuilt[1]);
    }

    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }


    private String parseBuildCount(String article) {
        return ReactiveUtil.findValue(article, "number built");
    }

}

