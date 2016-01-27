package com.senacor.tecco.reactive.concurrency.e1.synchronous;

import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Rule;
import org.junit.Test;

public class E13_Synchronous_SumMultiplePlanes extends PlaneArticleBaseTest {

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneBuildCountIsSummedUpSynchronously() throws Exception {

        //get article on 777
        String article777 = fetchArticle("Boeing 777");

        //get article on 747
        String article747 = fetchArticle("Boeing 747");

        //extract number of built planes and calculate sum
        int buildCountSum = parseBuildCountInt(article777) + parseBuildCountInt(article747);

        Summary.printCounter("777 and 747", buildCountSum);
    }

    // fetches an article from Wikipedia
    private String fetchArticle(String articleName) {
        return wikiService.fetchArticle(articleName);
    }
}
