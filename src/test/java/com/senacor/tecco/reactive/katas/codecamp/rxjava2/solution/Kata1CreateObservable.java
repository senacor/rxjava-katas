package com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceMediaWikiBot;
import io.reactivex.Observable;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle

        Observable.<Article>create(subscriber -> {
            try {
                subscriber.onNext(getArticle(articleName));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribe((toPrint) -> {
            ReactiveUtil.print(toPrint.getText());
        }, throwable -> System.err.println(throwable.getMessage()));
    }

    @Test
    public void createAnObservable2() throws Exception {
        final String articleName = "Observable";
        // Erzeuge aus getArticle ein Observable

        Observable.defer(() -> Observable.just(getArticle(articleName)))
                .subscribe((toPrint) -> ReactiveUtil.print(toPrint.getText()));
    }

    private Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
