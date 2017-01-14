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

        Observable.just(articleName)
            .map(this::getArticle)
            .subscribe(
                article -> {
                  System.out.println("Article: " + article.toString());
                },
                err -> {
                  System.err.println("Error: " + err.getMessage());
                },
                () -> {
                  System.out.println("done");
                }
            );
        };


    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
