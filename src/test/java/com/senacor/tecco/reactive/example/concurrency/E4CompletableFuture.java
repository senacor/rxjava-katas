package com.senacor.tecco.reactive.example.concurrency;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * Retrieves and combines plane information  CompletableFutures
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E4CompletableFuture {

    @Test
    public void thatPlaneInfosAreCombinedWithCompletableFuture() throws Exception {

        CompletableFuture<String> numberBuilt777Future = fetchArticle("Boeing 777")
                .thenApply(this::parseNumberBuilt);

        CompletableFuture<String> numberBuilt747Future = fetchArticle("Boeing 747")
                .thenApply(this::parseNumberBuilt);

        String planesBuilt = numberBuilt777Future
                .thenCombine(numberBuilt747Future, (numberBuilt777, numberBuilt747) -> {
                    return "747: " + numberBuilt747 + " 777: " + numberBuilt777;
                })
                .get();

        print(planesBuilt);
    }

    CompletableFuture<String> fetchArticle(String articleName){
        return WikiService.WIKI_SERVICE_EN.fetchArticleCompletableFuture(articleName);
    }

    String parseNumberBuilt(String article){
        return WikiService.WIKI_SERVICE_EN.findValue(article, "number built");
    }

}
