package com.senacor.tecco.codecamp.reactive.services;

import net.sourceforge.jwbf.core.actions.HttpActionClient;
import net.sourceforge.jwbf.core.contentRep.Article;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class WikipediaServiceMediaWikiBot {

    private HttpActionClient client = HttpActionClient.builder() //
            .withUrl("http://de.wikipedia.org/w/") //
            .withRequestsPerUnit(50, TimeUnit.SECONDS) //
            .build();
    private MediaWikiBot wikiBot = new MediaWikiBot(client);

    public static void main(String[] args) {
        WikipediaServiceMediaWikiBot wikipediaService = new WikipediaServiceMediaWikiBot();
        Article article = wikipediaService.getArticle("42");
        System.out.println("title=" + article.getTitle());
        System.out.println("editor=" + article.getEditor());
        System.out.println("text=" + article.getText());
    }

    public Article getArticle(String name) {
        return wikiBot.getArticle(name);
    }

    public Observable<Article> getArticleObservable(String wikiArticle) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(getArticle(wikiArticle));
                subscriber.onCompleted();
            } catch (RuntimeException e) {
                subscriber.onError(e);
            }
        });
    }
}
