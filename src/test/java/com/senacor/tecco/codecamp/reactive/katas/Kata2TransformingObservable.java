package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

    private static WikiService wikiService = new WikiService();

    @Test
    public void transformingObservable() throws Exception {
        // 1. Benutze den WikiService (fetchArticle) und hole dir einen beliebigen Wikipedia Artikel

        Observable<String> article = wikiService.fetchArticle("observable");

        // 2. Transformiere das Ergebnis mit Hilfe von WikiService#parseMediaWikiText in eine Objektstruktur
        // 3. gib den Wikipedia Artikel Text in der Console aus (ParsedPage.getText())

        article.flatMap(text -> wikiService.parseMediaWikiText(text))
                .subscribe(
                        item -> System.out.print(item.getText())
                );

        // WikiService.WIKI_SERVICE.fetchArticle()
    }

}
