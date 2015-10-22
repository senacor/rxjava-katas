package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;


/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

    @Test
    public void transformingObservable() throws Exception {
        WikiService.WIKI_SERVICE.fetchArticle("Unsinn")
                // Flatten Observable of Observable
                .flatMap(article -> WikiService.WIKI_SERVICE.parseMediaWikiText(article))
                .subscribe(
                        parsedPage -> System.out.print(parsedPage.getText()),
                        Throwable::printStackTrace,
                        () -> System.out.println("Fertig")
                );
        // 1. Benutze den WikiService (fetchArticle) und hole dir einen beliebigen Wikipedia Artikel
        // 2. Transformiere das Ergebnis mit Hilfe von WikiService#parseMediaWikiText in eine Objektstruktur
        // 3. gib den Wikipedia Artikel Text in der Console aus (ParsedPage.getText())

        // WikiService.WIKI_SERVICE.fetchArticle()
    }
}
