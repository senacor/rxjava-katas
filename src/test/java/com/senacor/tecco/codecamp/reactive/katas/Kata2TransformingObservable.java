package com.senacor.tecco.codecamp.reactive.katas;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;

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

        final String articleName = "42 (Antwort)";
        //        Observable.just(articleName).flatMap(WIKI_SERVICE::fetchArticle).flatMap(WIKI_SERVICE::parseMediaWikiText).map(ParsedPage::getText)
        //            .subscribe(ReactiveUtil::print, exception -> ReactiveUtil.print("Fehler: %s", exception.getMessage()));

        WIKI_SERVICE.fetchArticle(articleName).flatMap(WIKI_SERVICE::parseMediaWikiText).map(ParsedPage::getText)
            .subscribe(ReactiveUtil::print, exception -> ReactiveUtil.print("Fehler: %s", exception.getMessage()));
    }

}
