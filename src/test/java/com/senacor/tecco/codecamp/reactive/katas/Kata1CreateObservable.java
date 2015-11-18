package com.senacor.tecco.codecamp.reactive.katas;

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
        Observable.<Article>create(observer -> {
            try {
                observer.onNext(getArticle(articleName));
                observer.onCompleted();
            } catch (Exception e) {
                observer.onError(e);
            }

        }).subscribe(article -> {
            try {
                ReactiveUtil.print(article.getText());
            } catch (Exception e) {
                ReactiveUtil.print(article.getText());
            }

        });

    }

    @Test
    public void testDefer () {



    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
