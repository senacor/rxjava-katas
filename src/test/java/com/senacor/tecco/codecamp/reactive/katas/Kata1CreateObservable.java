package com.senacor.tecco.codecamp.reactive.katas;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikipediaServiceMediaWikiBot;

import net.sourceforge.jwbf.core.contentRep.Article;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

	@Test
	public void erzeugeEinObservable() throws Exception {
		final String articleName = "Observable";
		// Erzeuge aus getArticle ein Observable

		WaitMonitor monitor = new WaitMonitor();

		Observable.<Article>create(subscriber -> {
			try {
				subscriber.onNext(getArticle(articleName));
				subscriber.onNext(getArticle("Physik"));
				subscriber.onNext(getArticle("Beratung"));
			} catch (Exception e) {
				subscriber.onError(new NullPointerException("Some error occured"));
			}
			subscriber.onCompleted();
		})
		.map(article -> article.getText().length())
		.subscribe(
			length -> System.out.println(length), // on next
//			article -> System.out.println(article.getText()), // on next
			error -> error.printStackTrace(),
			//Throwable::printStackTrace, // on error
			() -> {System.out.println("completed"); monitor.complete();} // on completed
		);
		
		monitor.waitFor(15L, TimeUnit.SECONDS);
	}
	
	@Test
	public void useJust() {
		final String articleName = "Observable";
		// Erzeuge aus getArticle ein Observable
		
		WaitMonitor monitor = new WaitMonitor();

//		Observable.just(getArticle(articleName))
		Observable.defer(() -> Observable.just(getArticle(articleName)))
		.map(article -> article.getText().length())
		.subscribe(
			length -> System.out.println(length), // on next
//			article -> System.out.println(article.getText()), // on next
			error -> error.printStackTrace(),
			//Throwable::printStackTrace, // on error
			() -> {System.out.println("completed"); monitor.complete();} // on completed
		);
		
		monitor.waitFor(15L, TimeUnit.SECONDS);
		
	}

	public Article getArticle(String name) {
		return new WikipediaServiceMediaWikiBot().getArticle(name);
	}
}
