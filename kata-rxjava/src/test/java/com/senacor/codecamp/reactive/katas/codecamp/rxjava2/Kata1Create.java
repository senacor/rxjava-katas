package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceMediaWikiBot;
import io.reactivex.Observable;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

/**
 * @author Andreas Keefer
 */
public class Kata1Create {

    @Test
    @KataClassification(mandatory)
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle
        Observable<Article> articleObservable =
                Observable.fromCallable(() -> getArticle(articleName));

        articleObservable
                .map(Article::getText)
                .subscribe(System.out::println);
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
