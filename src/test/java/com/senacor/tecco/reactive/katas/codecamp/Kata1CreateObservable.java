package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.integration.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle
        Observable.just(articleName).map(this::getArticle)
                .subscribe(x -> System.out.println(x.getText()));
    }

    @Test
    public void createAnObservableSolution() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle
        Observable<String> article = Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(getArticle(articleName).getText());

                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

        article.subscribe(System.out::println);
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
