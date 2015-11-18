package com.senacor.tecco.codecamp.reactive.katas;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.services.WikipediaServiceMediaWikiBot;

import net.sourceforge.jwbf.core.contentRep.Article;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

    @Test
    public void erzeugeEinObservable() throws Exception {
        final String articleName = "Observable";
        // Erzeuge aus getArticle ein Observable
        //        Observable.just(articleName).map(this::getArticle).map(Article::getText).subscribe(System.out::println);

        Observable.<Article>create(observer -> {
            try {
                if (!observer.isUnsubscribed()) {
                    observer.onNext(getArticle(articleName));
                    observer.onCompleted();
                }
            } catch (Exception e) {
                observer.onError(e);
            }
        }).map(Article::getText).subscribe(ReactiveUtil::print, System.err::println, () -> System.out.println("done"));

        //        Observable.<String>create(observer -> {
        //            try {
        //                if (!observer.isUnsubscribed()) {
        //                    observer.onNext(getArticle(articleName).getText());
        //                    observer.onCompleted();
        //                }
        //            } catch (Exception e) {
        //                observer.onError(e);
        //            }
        //        }).subscribe(System.out::println, System.err::println, () -> System.out.println("done"));
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
