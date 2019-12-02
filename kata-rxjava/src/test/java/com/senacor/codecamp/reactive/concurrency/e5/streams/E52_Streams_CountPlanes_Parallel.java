package com.senacor.codecamp.reactive.concurrency.e5.streams;

import com.senacor.codecamp.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.codecamp.reactive.concurrency.Summary;
import com.senacor.codecamp.reactive.concurrency.model.Article;
import com.senacor.codecamp.reactive.concurrency.model.PlaneInfo;
import org.junit.Test;

import java.util.Arrays;

/**
 * Retrieves and combines plane information with streams
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E52_Streams_CountPlanes_Parallel extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsFetchedWithStreams() throws Exception {

        String[] planeTypes = {"Boeing 777", "Boeing 747"};

        Arrays.stream(planeTypes)
                //process stream in parallel. Due to blocking calls, multiple threads are blocked
                .parallel()
                .map(planeType -> fetchArticle(planeType))
                .map(article -> parsePlaneInfo(article))
                .forEach((planeInfo) -> {
                    Summary.printCounter(planeInfo.typeName, planeInfo.numberBuild);
                });
    }


    // fetches an article from Wikipedia
    Article fetchArticle(String articleName) {
        return new Article(articleName, wikiService.fetchArticle(articleName));
    }

    // Extracts plane-related information from an wikipedia article
    PlaneInfo parsePlaneInfo(Article article) {
        return new PlaneInfo(article.name, parseBuildCountInt(article.content));
    }

}
