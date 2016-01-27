package com.senacor.tecco.reactive.concurrency.e1.synchronous;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import org.junit.Test;

public class E12_Synchronous_SumPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildCountIsSummedUpSynchronously() throws Exception {

        String[] planes = {"Boeing 777","Boeing 747"};
        int buildCountSum = 0;

        for(String plane : planes){

            //get article
            String article  = fetchArticle(plane);

            //extract number of built planes and calculate sum
            buildCountSum += parseBuildCountInt(article);

        }
        Summary.printCounter(formatPlanes(planes), buildCountSum);
    }

    // fetches an article from Wikipedia
    private String fetchArticle(String articleName) {
        return wikiService.fetchArticle(articleName);
    }

}
