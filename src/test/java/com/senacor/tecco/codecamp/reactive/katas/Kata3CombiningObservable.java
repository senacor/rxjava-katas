package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.json.simple.JSONValue;
import org.junit.Test;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

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

    // WikiService.WIKI_SERVICE.fetchArticle()
    final String articleName = "Zurück in die Zukunft";

    final Observable<ParsedPage> parsedPageObservable =
            WikiService.WIKI_SERVICE.fetchArticle(articleName).flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText);
    final Observable<Integer> rateObservable = parsedPageObservable.flatMap(WikiService.WIKI_SERVICE::rate);
    final Observable<Integer> wordCountObservable = parsedPageObservable.flatMap(WikiService.WIKI_SERVICE::countWords);

    zip(parsedPageObservable, rateObservable, wordCountObservable, (parsedPage, rate, wordCount) -> {
      final Map<String, Object> jsonMap = new HashMap<>();
      jsonMap.put("articleName", parsedPage.getName());
      jsonMap.put("rating", rate);
      jsonMap.put("wordCount", wordCount);
      return jsonMap;
    })
            .map(JSONValue::toJSONString)
            .subscribe(jsonText -> System.out.println(jsonText));
  }

}
