package com.senacor.tecco.reactive.concurrency;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class E4CompletableFuture {

    private final WikiService wikiService = new WikiService("en");
    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Rule
    public final Watch watch = new Watch();


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

                    String numberBuild777 = parseNumberBuilt(article);
                    return pageMetrixFuture.thenAccept(pageMetrix -> Summary.print("777", pageMetrix.words, pageMetrix.rating, numberBuild777));
                });


        Future<Void> _747Future = fetchArticle("Boeing 747")
                .thenCompose(article -> {
                    CompletableFuture<ParsedPage> parsedPageFuture = parsePage(article);
                    CompletableFuture<PageMetrix> pageMetrixFuture = parsedPageFuture.thenCompose(page -> {
                        CompletableFuture<Integer> countFuture = countWords(page);
                        CompletableFuture<Integer> rateFuture = rateArticles(page);
                        return countFuture.thenCombine(rateFuture, (words, rating) -> new PageMetrix(words, rating));
                    });

                    String numberBuild777 = parseNumberBuilt(article);
                    return pageMetrixFuture.thenAccept(pageMetrix -> Summary.print("777", pageMetrix.words, pageMetrix.rating, numberBuild777));
                });

        _777Future.get();
        _747Future.get();

    }

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

    private String parseNumberBuilt(String article) {
        return ReactiveUtil.findValue(article, "number built");
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

