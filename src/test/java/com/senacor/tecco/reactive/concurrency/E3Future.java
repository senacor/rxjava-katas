package com.senacor.tecco.reactive.concurrency;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.Future;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

public class E3Future {

    private final WikiService wikiService = new WikiService("en");
    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Rule
    public final Watch watch = new Watch();

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

        String numberBuilt777 = parseNumberBuilt(article777);
        String numberBuilt747 = parseNumberBuilt(article747);

        Summary.print("777", words777Future.get(), rating777Future.get(), numberBuilt777);
        Summary.print("747", words747Future.get(), rating747Future.get(), numberBuilt747);
    }

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

    private String parseNumberBuilt(String article) {
        return ReactiveUtil.findValue(article, "number built");
    }


}
