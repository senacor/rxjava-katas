package com.senacor.tecco.reactive.concurrency.e6.observables;

import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.concurrency.model.Article;
import com.senacor.tecco.reactive.concurrency.model.PlaneInfo;
import org.junit.Test;
import rx.Observable;

public class E61_Observables_CountPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneInfoIsCombinedWithObservables_notPerfectYet() throws Exception {

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        Observable.from(planeTypes)
                .flatMap(this::fetchArticle)
                .map(this::parsePlaneInfo)
                .subscribe((planeInfo) -> {
                    Summary.printCounter(planeInfo.typeName, planeInfo.numberBuild);
                });
    }
        // fetches an article from the wikipedia
        Observable<Article> fetchArticle(String articleName) {
            return wikiService.fetchArticleObservable(articleName).
                    map((article) -> new Article(articleName, article));
        }

        // Extracts plane-related information from an wikipedia article
        PlaneInfo parsePlaneInfo(Article article){
            return new PlaneInfo(article.name, parseBuildCountInt(article.content));
        }




}
