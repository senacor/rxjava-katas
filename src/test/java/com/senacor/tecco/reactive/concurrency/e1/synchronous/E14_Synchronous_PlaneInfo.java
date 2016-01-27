package com.senacor.tecco.reactive.concurrency.e1.synchronous;

import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Rule;
import org.junit.Test;

public class E14_Synchronous_PlaneInfo extends PlaneArticleBaseTest {

    private final WikiService wikiService = new WikiService("en");
    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneInfoIsCombinedSynchronously() throws Exception {

        //get article on 777
        String article777 = fetchArticle("Boeing 777");

        //extract number of built planes
        String buildCount777 = parseBuildCount(article777);

        //extract Article Information
        ParsedPage parsedPage777 = parsePage(article777);
        int words777 = countWords(parsedPage777);
        int rating777 = rateArticles(parsedPage777);

        //get article on 747
        String article747 = fetchArticle("Boeing 747");

        //extract number of built planes
        String buildCount747 = parseBuildCount(article747);

        //extract Article Information
        ParsedPage parsedPage747 = parsePage(article747);
        int words747 = countWords(parsedPage747);
        int rating747 = rateArticles(parsedPage747);

        Summary.print("777", words777, rating777, buildCount777);
        Summary.print("747", words747, rating747, buildCount747);
    }

    // fetches an article from Wikipedia
    private String fetchArticle(String articleName) {
        return wikiService.fetchArticle(articleName);
    }

    private ParsedPage parsePage(String article777) {
        return wikiService.parseMediaWikiText(article777);
    }

    private int countWords(ParsedPage parsedPage) {
        return countService.countWords(parsedPage);
    }

    private int rateArticles(ParsedPage parsedPage) {
        return ratingService.rate(parsedPage);
    }

}
