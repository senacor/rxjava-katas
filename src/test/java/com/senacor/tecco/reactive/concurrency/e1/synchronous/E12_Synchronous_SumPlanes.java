package com.senacor.tecco.reactive.concurrency.e1.synchronous;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Rule;
import org.junit.Test;

public class E12_Synchronous_SumPlanes {

    private final WikiService wikiService = new WikiService("en");
    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneBuildCountIsSummedUpSynchronously() throws Exception {

        //get article on 777
        String article777 = fetchArticle("Boeing 777");

        //get article on 747
        String article747 = fetchArticle("Boeing 747");

        //extract number of built planes and calculate sum
        int buildCountSum = parseBuildCount(article777) + parseBuildCount(article747);

        Summary.printCounter("777 and 747", buildCountSum);
    }

    private String fetchArticle(String articleName) {
        return wikiService.fetchArticle(articleName);
    }

    private int parseBuildCount(String article) {
        String buildCount = ReactiveUtil.findValue(article, "number built");
        return Integer.parseInt(buildCount.replaceAll(",",""));
    }


}
