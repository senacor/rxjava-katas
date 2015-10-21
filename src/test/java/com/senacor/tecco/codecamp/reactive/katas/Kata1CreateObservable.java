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
        Observable.<Article>create(subscriber -> {

            try {
                subscriber.onNext(getArticle(articleName));
                subscriber.onNext(getArticle("Physik"));
                subscriber.onNext(getArticle("Beratung"));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(new IllegalArgumentException());
            }
        })
                .map(article -> article.getText().length())
                .subscribe(
                        item -> System.out.println(item), //on next
                        Throwable::printStackTrace, //on error
                        () -> System.out.println("completed")//on complete
                );
    }

    @Test
    public void useJust() {
        final String articleName = "Observable";
        // Erzeuge aus getArticle ein Observable
        Observable.defer(() -> Observable.just(getArticle(articleName))
                .map(article -> article.getText().length())
        ).subscribe(
                item -> System.out.println(item), //on next
                Throwable::printStackTrace, //on error
                () -> System.out.println("completed")//on complete
        );
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
