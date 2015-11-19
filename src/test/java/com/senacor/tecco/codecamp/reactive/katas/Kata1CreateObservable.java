package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.services.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import rx.Observable;

import java.util.Collections;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

    @Test
    public void erzeugeEinObservable() throws Exception {
        final String articleName = "Observable";
        Observable<Article> justObservable = Observable.just(getArticle(articleName));

        Observable<Article> createObservable = Observable.create(s -> {
            s.onNext(getArticle(articleName));
//            s.onCompleted();
        });

        Observable<Article> fromObservable = Observable.from(Collections.singletonList(getArticle(articleName)));


        justObservable.subscribe(a -> ReactiveUtil.print(a.getTitle()),
                                 e -> {},
                                () -> ReactiveUtil.print("Complete")
                                );
        ReactiveUtil.print("----------------");
        createObservable.subscribe(a -> ReactiveUtil.print(a.getTitle()),
                e -> {},
                () -> ReactiveUtil.print("Complete")
        );
        ReactiveUtil.print("----------------");
        fromObservable.subscribe(a -> ReactiveUtil.print(a.getTitle()));
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
