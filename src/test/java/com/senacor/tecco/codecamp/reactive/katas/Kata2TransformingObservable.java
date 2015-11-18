package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import rx.Observable;

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


        final WikiService wikiService = new WikiService();

        wikiService.fetchArticle("Oracle").flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText).subscribe(page -> {
            ReactiveUtil.print(page.getText());
        }

        );


    }



}
