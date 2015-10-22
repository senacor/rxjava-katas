package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
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

        // Erzeuge aus n x getArticle ein Observable
        rx.Observable.<Article>create(subscriber -> {
            try {
                subscriber.onNext(getArticle(articleName));
                subscriber.onNext(getArticle("AndererArtikel"));
                subscriber.onNext(getArticle("NochmalAnders"));
            }
            catch(Exception e){
                // Wellformed -> 1 x onError()
                subscriber.onError(new IllegalArgumentException("kaputt"));
            }

            // fertisch
            subscriber.onCompleted();
        })
                .map(article -> article.getText().length())
                .subscribe(
                    item -> System.out.print(item),
                        //article -> System.out.println(article.toString()),
                    Throwable::printStackTrace,
                    () -> System.out.println("Fertig")
        );
    }


    @Test
    public void erzeugeEinObservableMitJust() throws Exception {
        final String articleName = "Observable";

        final WaitMonitor waitMonitor = new WaitMonitor();

        // Imidiate call
        Observable.just(getArticle(articleName))
            .map(article -> article.getText().length())
            .subscribe(
                    item -> System.out.print(item),
                    //article -> System.out.println(article.toString()),
                    Throwable::printStackTrace,
                    () -> System.out.println("Fertig")
            );

    }


    @Test
    public void erzeugeEinObservableMitJustUndDefer() throws Exception {
        final String articleName = "Observable";

        // Imidiate call
        Observable
            .defer(() -> Observable.just(getArticle(articleName)))
            .map(article -> article.getText().length())
            .subscribe(
                    item -> System.out.print(item),
                    //article -> System.out.println(article.toString()),
                    Throwable::printStackTrace,
                    () -> System.out.println("Fertig")
            );
    }

    @Test
    public void erzeugeEinObservableMitThread() throws Exception {
        final String articleName = "Observable";

        // replace (anonymous) with lambda?
        new Runnable() {
            @Override
            public void run() {
                // Erzeuge aus getArticle ein Observable
                rx.Observable.<Article>create(subscriber -> {
                    try {
                        subscriber.onNext(getArticle(articleName));
                    } catch (Exception e) {
                        subscriber.onError(new IllegalArgumentException("kaputt"));
                    }

                    subscriber.onCompleted();
                })
                        .map(article -> article.getText().length())
                        .subscribe(
                                item -> System.out.print(item),
                                //article -> System.out.println(article.toString()),
                                Throwable::printStackTrace,
                                () -> System.out.println("Fertig")
                                // innext
                                // inerror
                                // ..
                        );
            }
        }.run();

    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
