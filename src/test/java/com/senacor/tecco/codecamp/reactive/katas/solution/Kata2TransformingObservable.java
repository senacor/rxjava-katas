package com.senacor.tecco.codecamp.reactive.katas.solution;

import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.junit.Test;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

    @Test
    public void transformingObservable() throws Exception {
        // 1. Benutze den WikiService (fetchArticle) und hole dir einen beliebigen Wikipedia Artikel
        // 2. Transformiere das Ergebnis mit Hilfe von WikiService#parseMediaWikiText in eine Objektstruktur
        // 3. gib den Wikipedia Artikel Text in der Console aus (ParsedPage.getText())

        WikiService.WIKI_SERVICE.fetchArticle("Bilbilis")
                .doOnNext(debug -> print("fetchArticle res: %s", debug))
                .flatMap(WIKI_SERVICE::parseMediaWikiText)
                .doOnNext(debug -> print("parseMediaWikiText res: %s", debug))
                .subscribe(next -> print("next: %s", next.getText()),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

    }


}
