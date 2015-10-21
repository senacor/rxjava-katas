package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

    @Test
    public void erzeugeEinObservable() throws Exception {
        final String articleName = "Observable";
        // Erzeuge aus getArticle ein Observable

        WaitMonitor waitMonitor = new WaitMonitor();

        Observable.<Article>create(subscriber -> {
            try {
                subscriber.onNext(getArticle(articleName));
                subscriber.onNext(getArticle("Physik"));
                subscriber.onNext(getArticle("Banane"));
            } catch (Exception e) {
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        })
                .map(article -> article.getText().length())
                .subscribe(item -> System.out.println(item),
                        Throwable::printStackTrace,
                        () -> waitMonitor.complete());

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }

    public void test2() {
        final String articleName = "Observable";
        // Erzeuge aus getArticle ein Observable

        Observable<Article> one = // typisierung
                Observable.defer( // nicht eager die Liste erzeugen
                        ()-> Observable.<Article>just( // ein Element erzeugen
                                getArticle(articleName)));

        one.map(article -> article.getText().length()) // Mapping von Article auf seine Länge
                .subscribe(item -> System.out.println(item), // los gehts und auslösen
                        Throwable::printStackTrace, // Fehlerbehandlung
                        () -> System.out.println("completed")); // complete Aufruf des Observables behandeln
    }
}
