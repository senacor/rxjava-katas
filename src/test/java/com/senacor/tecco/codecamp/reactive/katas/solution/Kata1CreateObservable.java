package com.senacor.tecco.codecamp.reactive.katas.solution;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.services.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

    @Test
    public void erzeugeEinObservable() throws Exception {
        final String articleName = "Observable";
        // Erzeuge aus getArticle ein Observable

        Observable.<Article>create(subscriber -> {
            try {
                subscriber.onNext(getArticle(articleName));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribe((toPrint) -> {
            ReactiveUtil.print(toPrint.getText());
        }, throwable -> System.err.println(throwable.getMessage()));
    }

    @Test
    public void erzeugeEinObservable2() throws Exception {
        final String articleName = "Observable";
        // Erzeuge aus getArticle ein Observable

        Observable.defer(() -> Observable.just(getArticle(articleName)))
                .subscribe((toPrint) -> {
                    ReactiveUtil.print(toPrint.getText());
                });
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
