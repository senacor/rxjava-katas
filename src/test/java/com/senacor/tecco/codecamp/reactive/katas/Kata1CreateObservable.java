package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

  @Test
  public void erzeugeEinObservable() throws Exception {
    final String articleName = "Observable";
    // Erzeuge aus getArticle ein Observable

    WaitMonitor waitMonitor = new WaitMonitor();

    Observable.<Article>create(subscriber -> {
      subscriber.onNext(getArticle(articleName));
      subscriber.onNext(getArticle("Banane"));
      subscriber.onNext(getArticle("Annanas"));
      subscriber.onCompleted();
    })
              .map(Article::getText)
              .subscribe(System.out::println,
                         Throwable::printStackTrace,
                         waitMonitor::complete);

    waitMonitor.waitFor(5, TimeUnit.SECONDS);
  }

  public Article getArticle(String name) {
    return new WikipediaServiceMediaWikiBot().getArticle(name);
  }
}
