package com.senacor.codecamp.reactive.concurrency.e3.future;

import com.senacor.codecamp.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.codecamp.reactive.concurrency.Summary;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.concurrent.Future;

public class E33_Future_PlaneInfo extends PlaneArticleBaseTest {

    private final CountService countService = CountService.create();
    private final RatingService ratingService = RatingService.create();

    @Test
    public void thatPlaneInfoIsCombinedWithFutures() throws Exception {

        Future<String> article777Future = fetchArticle("Boeing 777");
        Future<String> article747Future = fetchArticle("Boeing 747");

        String article777 = article777Future.get();
        String article747 = article747Future.get();

        Future<ParsedPage> parsedPage777Future = parsePage(article777);
        Future<ParsedPage> parsedPage747Future = parsePage(article747);

        ParsedPage parsedPage777 = parsedPage777Future.get();
        ParsedPage parsedPage747 = parsedPage747Future.get();

        Future<Integer> words777Future = countWords(parsedPage777);
        Future<Integer> words747Future = countWords(parsedPage747);

        Future<Integer> rating777Future = rateArticles(parsedPage777);
        Future<Integer> rating747Future = rateArticles(parsedPage747);

        String numberBuilt777 = parseBuildCount(article777);
        String numberBuilt747 = parseBuildCount(article747);

        Summary.print("777", words777Future.get(), rating777Future.get(), numberBuilt777);
        Summary.print("747", words747Future.get(), rating747Future.get(), numberBuilt747);
    }

    // fetches an article from Wikipedia
    private Future<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleFuture(articleName);
    }

    private Future<ParsedPage> parsePage(String article777) {
        return wikiService.parseMediaWikiTextFuture(article777);
    }

    private Future<Integer> countWords(ParsedPage parsedPage) {
        return countService.countWordsFuture(parsedPage);
    }

    private Future<Integer> rateArticles(ParsedPage parsedPage) {
        return ratingService.rateFuture(parsedPage);
    }
}
