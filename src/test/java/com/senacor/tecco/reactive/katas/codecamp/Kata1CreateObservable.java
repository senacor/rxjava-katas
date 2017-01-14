package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle
        Observable.create(subscriber -> {
            try {
                subscriber.onNext(getArticle(articleName));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        })
        .subscribe(next ->  ReactiveUtil.print(next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
