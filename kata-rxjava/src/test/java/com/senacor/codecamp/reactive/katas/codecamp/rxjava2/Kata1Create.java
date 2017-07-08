package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

import org.junit.Test;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceMediaWikiBot;

import io.reactivex.Observable;
import net.sourceforge.jwbf.core.contentRep.Article;

/**
 * @author Andreas Keefer
 */
public class Kata1Create {

    @Test
    @KataClassification(mandatory)
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle
        Observable.create(s -> {
        	try {
				s.onNext(getArticle(articleName));
				s.onComplete();
			} catch (Exception e) {
				s.onError(e);
			}
        }).subscribe(x -> System.out.println(x), throwable -> System.err.println(throwable)); 
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
