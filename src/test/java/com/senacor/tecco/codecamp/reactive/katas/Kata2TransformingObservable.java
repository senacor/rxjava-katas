package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

  @Test
  public void transformingObservable() throws Exception {
    // 1. Benutze den WikiService (fetchArticle) und hole dir einen beliebigen Wikipedia Artikel
    // 2. Transformiere das Ergebnis mit Hilfe von WikiService#parseMediaWikiText in eine Objektstruktur
    // 3. gib den Wikipedia Artikel Text in der Console aus (ParsedPage.getText())

    WaitMonitor waitMonitor = new WaitMonitor();

    WikiService.WIKI_SERVICE.fetchArticle("Banane")
                            .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                            .map(ParsedPage::getText)
                            .subscribe(System.out::println,
                                       Throwable::printStackTrace,
                                       waitMonitor::complete);

    waitMonitor.waitFor(5, TimeUnit.SECONDS);
  }

}
