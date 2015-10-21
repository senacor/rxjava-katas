package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;
import static rx.Observable.zip;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

  @Test
  public void combiningObservable() throws Exception {
    // 1. Wikiartikel holen und parsen
    // 2. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
    //    und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}

    ParsedPage todo = new ParsedPage();

    WaitMonitor waitMonitor = new WaitMonitor();

    WIKI_SERVICE.fetchArticle("Banane")
                .flatMap(WIKI_SERVICE::parseMediaWikiText)
                .flatMap(parsedPage -> zip(WIKI_SERVICE.rate(parsedPage),
                                           WIKI_SERVICE.countWords(parsedPage),
                                           (rate, wordCount) ->
                                                   "{\"firstParagraph\": " + parsedPage.getFirstParagraph() +
                                                   ", \"rating\": " + rate +
                                                   ", \"wordCount\": " + wordCount +
                                                   "}"))
                .subscribe(System.out::println,
                           Throwable::printStackTrace,
                           waitMonitor::complete);

    waitMonitor.waitFor(5, TimeUnit.SECONDS);
  }

}
