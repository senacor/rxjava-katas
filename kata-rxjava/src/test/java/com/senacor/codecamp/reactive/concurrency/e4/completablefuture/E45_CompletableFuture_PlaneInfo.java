package com.senacor.codecamp.reactive.concurrency.e4.completablefuture;

import com.senacor.codecamp.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.codecamp.reactive.concurrency.Summary;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class E45_CompletableFuture_PlaneInfo extends PlaneArticleBaseTest {

    private final CountService countService = CountService.create();
    private final RatingService ratingService = RatingService.create();

    @Test
    public void thatPlaneInfoIsCombinedWithCompletableFuture() throws Exception {

        Future<Void> _777Future = fetchArticle("Boeing 777")
                .thenCompose(article -> {
                    CompletableFuture<ParsedPage> parsedPageFuture = parsePage(article);
                    CompletableFuture<PageMetrix> pageMetrixFuture = parsedPageFuture.thenCompose(page -> {
                        CompletableFuture<Integer> countFuture = countWords(page);
                        CompletableFuture<Integer> rateFuture = rateArticles(page);
                        return countFuture.thenCombine(rateFuture, (words, rating) -> new PageMetrix(words, rating));
                    });

                    String numberBuild777 = parseBuildCount(article);
                    return pageMetrixFuture.thenAccept(pageMetrix -> Summary
                            .print("777", pageMetrix.words, pageMetrix.rating, numberBuild777));
                });


        Future<Void> _747Future = fetchArticle("Boeing 747")
                .thenCompose(article -> {
                    CompletableFuture<ParsedPage> parsedPageFuture = parsePage(article);
                    CompletableFuture<PageMetrix> pageMetrixFuture = parsedPageFuture.thenCompose(page -> {
                        CompletableFuture<Integer> countFuture = countWords(page);
                        CompletableFuture<Integer> rateFuture = rateArticles(page);
                        return countFuture.thenCombine(rateFuture, (words, rating) -> new PageMetrix(words, rating));
                    });

                    String numberBuild747 = parseBuildCount(article);
                    return pageMetrixFuture.thenAccept(pageMetrix -> Summary
                            .print("747", pageMetrix.words, pageMetrix.rating, numberBuild747));
                });


        CompletableFuture<String> article747 = fetchArticle("Boeing 747");

        CompletableFuture<ParsedPage> parsed747 = article747.thenCompose(this::parsePage);
        CompletableFuture<Integer> wordCount747 = parsed747.thenCompose(this::countWords);
        CompletableFuture<Integer> rating747 = parsed747.thenCompose(this::rateArticles);

        CompletableFuture<PageMetrix> pageMetrix747 = wordCount747.thenCombine(rating747, PageMetrix::new);
        CompletableFuture<String> numberBuild747 = article747.thenApply(this::parseBuildCount);

        CompletableFuture<Void> _747Future2 = pageMetrix747.thenAcceptBoth(numberBuild747, (metrix, buildNumb) ->
                Summary.print("747", metrix.words, metrix.rating, buildNumb)
        );

        _777Future.get();
        _747Future.get();
        _747Future2.get();
    }

    // fetches an article from Wikipedia
    private CompletableFuture<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleCompletableFuture(articleName);
    }

    private CompletableFuture<ParsedPage> parsePage(String article777) {
        return wikiService.parseMediaWikiTextCompletableFuture(article777);
    }

    private CompletableFuture<Integer> countWords(ParsedPage parsedPage) {
        return countService.countWordsCompletableFuture(parsedPage);
    }

    private CompletableFuture<Integer> rateArticles(ParsedPage parsedPage) {
        return ratingService.rateCompletableFuture(parsedPage);
    }

    private final static class PageMetrix {
        private final int words;
        private final int rating;

        private PageMetrix(int words, int rating) {
            this.words = words;
            this.rating = rating;
        }

        public int getWords() {
            return words;
        }

        public int getRating() {
            return rating;
        }
    }
}
