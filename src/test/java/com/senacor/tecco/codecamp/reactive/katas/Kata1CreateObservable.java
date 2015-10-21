package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.services.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import rx.Observable;

import static rx.Observable.defer;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

  @Test
  public void erzeugeEinObservable() throws Exception {
    final String articleName = "Observable";
    // Erzeuge aus getArticle ein Observable

    Observable.<Article>create(subscriber -> {
      try {
        subscriber.onNext(getArticle(articleName));
        subscriber.onNext(getArticle("Phsysik"));
        subscriber.onNext(getArticle("Beratung"));
      } catch (Exception e) {
        subscriber.onError(new RuntimeException(e));
      }
      subscriber.onCompleted();
    })
              .subscribe(
                      article -> System.out.println(article.getText()),
                      error -> error.printStackTrace(),
                      () -> System.out.println("completed")
                        );

  }

  @Test
  public void erzeugeEinObservable_mitJust() throws Exception {
    final String articleName = "Observable";
    // Erzeuge aus getArticle ein Observable

    final Observable<Article> oneElement = defer(() -> Observable.<Article>just(getArticle(articleName)));

    oneElement
              .subscribe(
                      article -> System.out.println(article.getText())
                        );

  }

  public Article getArticle(String name) {
    return new WikipediaServiceMediaWikiBot().getArticle(name);
  }
}
