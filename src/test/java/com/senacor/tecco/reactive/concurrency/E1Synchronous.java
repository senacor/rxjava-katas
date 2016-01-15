package com.senacor.tecco.reactive.concurrency;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Rule;
import org.junit.Test;

public class E1Synchronous {

    private final WikiService wikiService = new WikiService("en");
    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneInfoIsCombinedSynchronously() throws Exception {

        String article777 = fetchArticle("Boeing 777");
        ParsedPage parsedPage777 = parsePage(article777);
        int words777 = countWords(parsedPage777);
        int rating777 = rateArticles(parsedPage777);
        String numberBuilt777 = parseNumberBuilt(article777);


        String article747 = fetchArticle("Boeing 747");
        ParsedPage parsedPage747 = parsePage(article747);
        int words747 = countWords(parsedPage747);
        int rating747 = rateArticles(parsedPage747);
        String numberBuilt747 = parseNumberBuilt(article747);

        Summary.print("777", words777, rating777, numberBuilt777);
        Summary.print("747", words747, rating747, numberBuilt747);
    }

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

    private String parseNumberBuilt(String article) {
        return ReactiveUtil.findValue(article, "number built");
    }


}
