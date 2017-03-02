package com.senacor.tecco.reactive.concurrency.e6.observables;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.concurrency.model.Article;
import com.senacor.tecco.reactive.concurrency.model.PlaneInfo;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class E62_Observables_SumPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneBuildSumIsCalculatedWithObservables() throws Exception {
        WaitMonitor monitor = new WaitMonitor();

        // error handler function
        Consumer<Throwable> exceptionConsumer = (e) -> {
            e.printStackTrace();
            monitor.complete();
        };

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330"};

        Observable.fromArray(planeTypes)
                .flatMap(this::fetchArticle)
                .map(this::parsePlaneInfo)
                .reduce(this::reducePlaneInfo)
                .subscribe((planeInfo) -> {
                            Summary.printCounter(planeInfo.typeName, planeInfo.numberBuild);
                        },
                        exceptionConsumer,
                        monitor::complete);

        monitor.waitFor(10000, TimeUnit.MILLISECONDS);
    }

    // fetches an article from the wikipedia
    Observable<Article> fetchArticle(String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .map((article) -> new Article(articleName, article));
    }

    // Extracts plane-related information from an wikipedia article
    PlaneInfo parsePlaneInfo(Article article) {
        String buildCount = ReactiveUtil.findValue(article.content, "number built");
        return new PlaneInfo(article.name, Integer.parseInt(buildCount.replaceAll(",", "")));
    }

    // reduces plane information by summing up the build counter
    PlaneInfo reducePlaneInfo(PlaneInfo planeInfoSum, PlaneInfo planeInfo) {
        planeInfoSum.numberBuild += planeInfo.numberBuild;
        planeInfoSum.appendTypeName(" and " + planeInfo.typeName);
        return planeInfoSum;
    }
}
