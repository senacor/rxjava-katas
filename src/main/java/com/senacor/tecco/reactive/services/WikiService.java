package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Andreas Keefer
 */
public interface WikiService {

    static WikiService create() {
        return create(DelayFunction.staticDelay(1000), FlakinessFunction.noFlakiness(), false, "de");
    }

    static WikiService create(String language) {
        return create(DelayFunction.staticDelay(1000), FlakinessFunction.noFlakiness(), false, language);
    }

    static WikiService create(DelayFunction delayFunction) {
        return create(delayFunction, FlakinessFunction.noFlakiness(), false, "de");
    }

    static WikiService create(FlakinessFunction flakinessFunction) {
        return create(DelayFunction.staticDelay(1000), flakinessFunction, false, "de");
    }

    /**
     * @param delayFunction
     * @param flakinessFunction
     * @param mockMode          true=Mock, false=Wikipedia remote call
     * @param language          de|en
     * @return
     */
    static WikiService create(DelayFunction delayFunction,
                              FlakinessFunction flakinessFunction,
                              boolean mockMode,
                              String language) {
        return StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new WikiServiceImpl(mockMode, language), flakinessFunction)
                        , delayFunction));
    }

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formatted string
     */
    Flux<String> fetchArticleFlux(String wikiArticle);

    Flowable<String> fetchArticleFlowable(String wikiArticle);

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formatted string
     */
    Observable<String> fetchArticleObservable(String wikiArticle);

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formatted string
     */
    String fetchArticle(String wikiArticle);

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formatted string
     */
    void fetchArticleCallback(String wikiArticle, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer);

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formatted string
     */
    Future<String> fetchArticleFuture(String wikiArticle);

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formatted string
     */
    CompletableFuture<String> fetchArticleCompletableFuture(String wikiArticle);

    /**
     * @param mediaWikiText Media Wiki formatted text
     * @return parsed text in structured form
     */
    Flux<ParsedPage> parseMediaWikiTextFlux(String mediaWikiText);

    Observable<ParsedPage> parseMediaWikiTextObservable(String mediaWikiText);

    /**
     * @param mediaWikiText Media Wiki formatted text
     * @return parsed text in structured form
     */
    ParsedPage parseMediaWikiText(String mediaWikiText);

    Future<ParsedPage> parseMediaWikiTextFuture(String mediaWikiText);

    CompletableFuture<ParsedPage> parseMediaWikiTextCompletableFuture(String mediaWikiText);

    /**
     * Erzeugt "ArticleBeingRead"-Events in der angegebenen Frequenz, also als Stream
     *
     * @param interval interval size in time units (see below)
     * @param unit     time units to use for the interval size
     * @return Wiki Artikel, der gerade gelesen wird.
     * ATTENTION, this is a HOT observable, which emits the Items without a subscription
     */
    Flux<String> wikiArticleBeingReadFlux(long interval, TimeUnit unit);

    /**
     * Erzeugt "ArticleBeingRead"-Events in der angegebenen Frequenz, also als Stream
     *
     * @param interval interval size in time units (see below)
     * @param unit     time units to use for the interval size
     * @return Wiki Artikel, der gerade gelesen wird.
     * ATTENTION, this is a HOT observable, which emits the Items without a subscription
     */
    Observable<String> wikiArticleBeingReadObservable(long interval, TimeUnit unit);

    /**
     * Erzeugt "ArticleBeingRead"-Events, also als Stream.
     * - Es gibt immer wieder Bursts, dann kommen sehr viele Elemente in sehr kurzer Zeit
     *
     * @return Wiki Artikel, der gerade gelesen wird
     * ATTENTION, this is a HOT observable, which emits the Items without a subscription
     */
    Flux<String> wikiArticleBeingReadFluxBurst();

    /**
     * Erzeugt "ArticleBeingRead"-Events, also als Stream.
     * - Es gibt immer wieder Bursts, dann kommen sehr viele Elemente in sehr kurzer Zeit
     *
     * @return Wiki Artikel, der gerade gelesen wird
     * ATTENTION, this is a HOT observable, which emits the Items without a subscription
     */
    Observable<String> wikiArticleBeingReadObservableBurst();

    /**
     * Erzeugt "ArticleBeingRead"-Events, also als Stream.
     * - Es gibt immer wieder Bursts, dann kommen sehr viele Elemente in sehr kurzer Zeit
     * - Manchmal gibt es einen Fehler (Exception)
     *
     * @return Wiki Artikel, der gerade gelesen wird
     * ATTENTION, this is a HOT observable, which emits the Items without a subscription
     */
    Observable<String> wikiArticleBeingReadObservableWithRandomErrors();
}
