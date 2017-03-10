package com.senacor.codecamp.reactive.concurrency.e6.observables;

import com.senacor.codecamp.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.codecamp.reactive.concurrency.Summary;
import com.senacor.codecamp.reactive.concurrency.model.PlaneInfo;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import com.senacor.codecamp.reactive.concurrency.model.Article;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class E61_Observables_CountPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneInfoIsSummedUpCombinedWithObservables() throws Exception {
        WaitMonitor monitor = new WaitMonitor();

        // error handler function
        Consumer<? super Throwable> exceptionConsumer = (e) -> {
            e.printStackTrace();
            monitor.complete();
        };

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330"};

        Observable.fromArray(planeTypes)
                .flatMap(this::fetchArticle)
                .map(this::parsePlaneInfo)
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
        return new PlaneInfo(article.name, parseBuildCountInt(article.content));
    }

}
