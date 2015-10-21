package com.senacor.tecco.codecamp.reactive.katas;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.services.WikiService;

import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

	@Test
	public void transformingObservable() throws Exception {
		// 1. Benutze den WikiService (fetchArticle) und hole dir einen
		// beliebigen Wikipedia Artikel
		WikiService.WIKI_SERVICE.fetchArticle("Elektrotechnik")
		// 2. Transformiere das Ergebnis mit Hilfe von WikiService#parseMediaWikiText in eine Objektstruktur
				.flatMap(str -> WikiService.WIKI_SERVICE.parseMediaWikiText(str))
				// 3. gib den Wikipedia Artikel Text in der Console aus
				.subscribe(text -> System.out.println(text.getText()));
		//
		
//		Thread.sleep(10000L);
	}

}
