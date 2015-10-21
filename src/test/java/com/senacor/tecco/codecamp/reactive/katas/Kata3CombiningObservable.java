package com.senacor.tecco.codecamp.reactive.katas;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.services.WikiService;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import rx.Observable;
import rx.observables.JoinObservable;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

	@Test
	public void combiningObservable() throws Exception {
		// 1. Wikiartikel holen und parsen
		// 2. Benutze jetzt den WikiService#rate() und #countWords() und
		// kombiniere beides im JSON-Format
		// und gib das JSON auf der Console aus. Beispiel {"articleName":
		// "Superman", "rating": 3, "wordCount": 452}

		// 
		String articleName = "Elektrotechnik";
		Observable<ParsedPage> obs = 
				WikiService.WIKI_SERVICE.fetchArticle(articleName)
				.<ParsedPage>flatMap(text -> WikiService.WIKI_SERVICE.parseMediaWikiText(text));
		
		Observable<Integer> obsRating = obs.flatMap(parsedPage -> WikiService.WIKI_SERVICE.rate(parsedPage));
		Observable<Integer> obsCount = obs.flatMap(parsedPage -> WikiService.WIKI_SERVICE.countWords(parsedPage));

//		Observable<String> obsZIP = 
		String JSON = "{\"articleName\":\"%s\", \"rating\":%d, \"wordCount\":%d}";
				Observable.zip(obsRating, obsCount, (rating, count) -> String.format(JSON, articleName , rating, count))
		.subscribe(System.out::println);
	}

}
