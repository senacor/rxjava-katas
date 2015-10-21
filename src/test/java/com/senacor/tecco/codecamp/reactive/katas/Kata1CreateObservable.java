package com.senacor.tecco.codecamp.reactive.katas;

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

        Observable.create(subscriber -> {
            subscriber.onNext(getArticle(articleName));
            subscriber.onCompleted();
        }).subscribe(
                // handle item
                article -> System.out.println(article),
                Throwable::printStackTrace,
                () -> System.out.println("erzeugeEinObservable Completed.")
                // on next
                // on error
                // on complete
        );

        Observable.<Article>defer(() -> Observable.create(subscriber -> {
            try {

                subscriber.onNext(getArticle(articleName));
                subscriber.onNext(getArticle("Physik"));
                subscriber.onNext(getArticle("Mathematik"));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(new RuntimeException("Something went wrong. Disconnect from Observable."));
            }
        })).map(article -> article.getText().length())
                .subscribe(
                        // handle item
                        article -> System.out.println(article),
                        Throwable::printStackTrace,
                        () -> System.out.println("erzeugeEinObservable Completed.")
                        // on next
                        // on error
                        // on complete
                );

    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
