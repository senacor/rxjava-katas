package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.integration.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        rx.Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(getArticle(articleName).getText());

                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        })
                .subscribe(System.out::println);

    }

    private Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
