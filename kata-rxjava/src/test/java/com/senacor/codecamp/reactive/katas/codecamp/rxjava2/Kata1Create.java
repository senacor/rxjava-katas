package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceMediaWikiBot;

import io.reactivex.Observable;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

import java.util.concurrent.Callable;

/**
 * @author Andreas Keefer
 */
public class Kata1Create {

    @Test
    @KataClassification(mandatory)
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle

        Observable.fromCallable(new Callable<String>() {
		        	@Override
		        	public String call() {
		        		return getArticle(articleName).getTitle();
		        	}
		        })
        		.subscribe(title -> System.out.println(title));
        
        // Alternative
        Observable.fromCallable(() -> getArticle(articleName).getTitle())
        		.subscribe(System.out::println);
        
        // Alternative
        Observable.just(articleName)
		        .map(this::getArticle)
		        .map(Article::getTitle)
		        .subscribe(System.out::println);
    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
