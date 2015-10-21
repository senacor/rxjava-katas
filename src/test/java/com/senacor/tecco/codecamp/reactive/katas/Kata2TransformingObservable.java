package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

    @Test
    public void transformingObservable() throws Exception {
        // 1. Benutze den WikiService (fetchArticle) und hole dir einen beliebigen Wikipedia Artikel
        // 2. Transformiere das Ergebnis mit Hilfe von WikiService#parseMediaWikiText in eine Objektstruktur
        // 3. gib den Wikipedia Artikel Text in der Console aus (ParsedPage.getText())

        // WikiService.WIKI_SERVICE.fetchArticle()

        final String articleName = "Zurück in die Zukunft";

        WikiService.WIKI_SERVICE.fetchArticle(articleName)
//                                .flatMap(mediaWikiText -> WikiService.WIKI_SERVICE.parseMediaWikiText(mediaWikiText))
                                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                .subscribe(article -> System.out.println(article.getText()));

    }

}
