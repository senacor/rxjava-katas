package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceMediaWikiBot;

import io.reactivex.Observable;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Andreas Keefer
 */
public class Kata1Create {

    @Test
    @KataClassification(mandatory)
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle
        
        Observable.fromCallable(() -> {
        	Article article = getArticle(articleName);
        	return article.getTitle();
        })
        .test()
        .assertValue("Observable")
        .assertComplete();
        
        Observable.just(articleName)
        .map(this::getArticle)
        .map(Article::getTitle)
        .test()
        .assertValue(articleName)
        .assertComplete();
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
