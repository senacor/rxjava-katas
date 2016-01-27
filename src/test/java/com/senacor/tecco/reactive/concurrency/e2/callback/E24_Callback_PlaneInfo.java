package com.senacor.tecco.reactive.concurrency.e2.callback;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Retrieves and combines plane information with callbacks
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E24_Callback_PlaneInfo extends PlaneArticleBaseTest {

    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    // error handler function
    Consumer<Exception> exceptionConsumer = (e)->{e.printStackTrace();};

    @Test
    public void thatPlaneInfosAreCombineWithCallback() throws Exception {
        LinkedBlockingQueue<PlaneArticleInfo> planeInfos = new LinkedBlockingQueue<>();

        fetchArticle("Boeing 777", (article777) -> {
            fetchArticle("Boeing 747", (article747) -> {

                PlaneArticleInfo articleInfo777 = new PlaneArticleInfo("777");
                ParsedPage parsedPage777 = parsePage(article777);
                articleInfo777.wordCound = countWords(parsedPage777);
                articleInfo777.rating = rateArticles(parsedPage777);
                articleInfo777.planeBuildCount = parseBuildCount(article777);

                PlaneArticleInfo articleInfo747 = new PlaneArticleInfo("747");
                ParsedPage parsedPage747 = parsePage(article747);
                articleInfo747.wordCound = countWords(parsedPage747);
                articleInfo747.rating = rateArticles(parsedPage747);
                articleInfo747.planeBuildCount = parseBuildCount(article747);

                try {
                    planeInfos.put(articleInfo777);
                    planeInfos.put(articleInfo747);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }, exceptionConsumer);
        }, exceptionConsumer);

        PlaneArticleInfo articleInfo = planeInfos.poll(5, TimeUnit.SECONDS);
        Summary.print(articleInfo.typeName, articleInfo.wordCound, articleInfo.rating, articleInfo.planeBuildCount);
        articleInfo = planeInfos.poll(5, TimeUnit.SECONDS);
        Summary.print(articleInfo.typeName, articleInfo.wordCound, articleInfo.rating, articleInfo.planeBuildCount);

    }

    // fetches an article from Wikipedia
    void fetchArticle(String articleName, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer) {
        wikiService.fetchArticleCallback(articleName, articleConsumer, exceptionConsumer);
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

    class PlaneArticleInfo{
        public String typeName;
        public String planeBuildCount;
        public int wordCound;
        public int rating;

        public PlaneArticleInfo(String typeName) {
            this.typeName = typeName;
        }
    }


}
