package com.senacor.tecco.codecamp.reactive.katas;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

	@Test
	public void schedulingObservable() throws Exception {
		// 1. Benutze den WikiService#wikiArticleBeingReadObservable, der einen
		// Stream von WikiArtikel Namen liefert, die gerade gelesen werden
		// 2. nim nur die ersten 20 Artikel
		// 3. lade und parse die Artikel
		// 4. Benutze jetzt den WikiService#rate() und #countWords() und
		// kombiniere beides im JSON-Format
		// und gib das JSON auf der Console aus. Beispiel {"rating": 3,
		// "wordCount": 452}
		// 5. messe die Laufzeit
		// 6. Füge jetzt an passender Stelle in der Observable-Chain einen
		// Schduler ein um die Ausführungszeit zu verkürzen

		Scheduler schedulerIO = Schedulers.io();
		Scheduler schedulerComputation = Schedulers.computation();
		
		WaitMonitor monitor = new WaitMonitor();
		
		WIKI_SERVICE.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
		.take(20)
		.flatMap(articleName -> WikiService.WIKI_SERVICE.fetchArticle(articleName).subscribeOn(schedulerIO))
		.observeOn(schedulerComputation)
		.flatMap(article -> WIKI_SERVICE.parseMediaWikiText(article))
		.flatMap(parsedPage -> Observable.zip(
				WIKI_SERVICE.rate(parsedPage),
				WIKI_SERVICE.countWords(parsedPage), 
				(rating, count) -> String.format("%d,%d",  rating, count)))
		.subscribe(System.out::println, Throwable::printStackTrace, () -> monitor.complete());
		
		monitor.waitFor(20, TimeUnit.SECONDS);
	}

}
