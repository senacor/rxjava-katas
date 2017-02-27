package com.senacor.tecco.reactive.services.integration;

import io.reactivex.Observable;
import net.sourceforge.jwbf.core.actions.HttpActionClient;
import net.sourceforge.jwbf.core.contentRep.Article;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class WikipediaServiceMediaWikiBot {

    private final HttpActionClient client = HttpActionClient.builder() //
            .withUrl("http://de.wikipedia.org/w/") //
            .withRequestsPerUnit(50, TimeUnit.SECONDS) //
            .build();
    private final MediaWikiBot wikiBot = new MediaWikiBot(client);

    public Article getArticle(String name) {
        return wikiBot.getArticle(name);
    }

    public Observable<Article> getArticleObservable(String wikiArticle) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(getArticle(wikiArticle));
                subscriber.onComplete();
            } catch (RuntimeException e) {
                subscriber.onError(e);
            }
        });
    }
}
