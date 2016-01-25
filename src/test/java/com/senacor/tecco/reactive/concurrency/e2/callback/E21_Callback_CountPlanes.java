package com.senacor.tecco.reactive.concurrency.e2.callback;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Retrieves and combines plane information with callbacks
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E21_Callback_CountPlanes {

    private final WikiService wikiService = new WikiService("en");

    // error handler function
    Consumer<Exception> exceptionConsumer = (e)->{e.printStackTrace();};

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneInfosAreCombineWithCallback() throws Exception {
        LinkedBlockingQueue<String[]> planeBuildCounts = new LinkedBlockingQueue<>();

        fetchArticle("Boeing 777", (article777) -> {
            //parse build numbers and add results to planeBuildCounts
            try {
                planeBuildCounts.put(new String[]{"777", parseBuildCount(article777)});
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
                //TODO: Das noch besser machen. Was passiert hier bei einer Exception...
                //TODO: Vielleicht hier exceptionConsumer aufrufen
            }
        }, exceptionConsumer);

        fetchArticle("Boeing 747", (article747) -> {
            //parse build numbers and add results to planeBuildCounts
            try {
                planeBuildCounts.put(new String[]{"747", parseBuildCount(article747)});
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, exceptionConsumer);

        Summary.printCounter(planeBuildCounts.poll(5, TimeUnit.SECONDS));
        Summary.printCounter(planeBuildCounts.poll(5, TimeUnit.SECONDS));
    }

    void fetchArticle(String articleName, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer) {
        wikiService.fetchArticleCallback(articleName, articleConsumer, exceptionConsumer);
    }

    String parseBuildCount(String article){
        return ReactiveUtil.findValue(article, "number built");
    }

}
