package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.services.integration.MediaWikiTextParser;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapi;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapiImpl;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapiMock;
import com.senacor.tecco.reactive.util.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static com.senacor.tecco.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static io.reactivex.BackpressureStrategy.MISSING;

/**
 * @author Andreas Keefer
 */
public class WikiServiceImpl implements WikiService {

    private final MediaWikiTextParser parser = new MediaWikiTextParser();
    private static final boolean MOCKMODE = false;
    private static final boolean RECORD = false;

    /**
     * when true, the results from fetchArticleObservable will be recorded for use in MOCKMODE;
     */
    private final boolean record;

    private final WikipediaServiceJapi wikiServiceJapi;

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    /**
     * @param mockMode when true, the Articles are fetched from a local file instead from Wikipedia.
     * @param language (de|en)
     */
    WikiServiceImpl(DelayFunction delayFunction,
                    FlakinessFunction flakinessFunction,
                    boolean mockMode,
                    String language) {
        WikipediaServiceJapi backend;
        if (MOCKMODE || mockMode) {
            this.record = false;
            backend = new WikipediaServiceJapiMock();
        } else {
            this.record = RECORD;
            backend = new WikipediaServiceJapiImpl("https://" + language + ".wikipedia.org");
        }
        this.wikiServiceJapi = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(backend, flakinessFunction)
                        , delayFunction));
    }

    @Override
    public Flux<String> fetchArticleFlux(final String wikiArticle) {
        return Flux.just(wikiArticle)
                .map(this::fetchArticle)
                .doOnNext(record(wikiArticle));
    }

    @Override
    public Flowable<String> fetchArticleFlowable(final String wikiArticle) {
        return Flowable.fromPublisher(fetchArticleFlux(wikiArticle));
    }

    @Override
    public Observable<String> fetchArticleObservable(final String wikiArticle) {
        return Observable.fromPublisher(fetchArticleFlux(wikiArticle));
    }

    private Consumer<String> record(String wikiArticle) {
        return article -> {
            if (record && StringUtils.isNotBlank(article)) {
                try {
                    Path path = Paths.get("./src/main/resources/mock/" + wikiArticle + ".txt");
                    if (path.toFile().exists()) {
                        print("RECORDING: mock data for Article '%s' already exists: %s", wikiArticle, path);
                    } else {
                        print("RECORDING: writing Article '%s' to %s", wikiArticle, path);
                        Files.write(path, article.getBytes());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public String fetchArticle(final String wikiArticle) {
        return wikiServiceJapi.getArticle(wikiArticle);
    }

    @Override
    public void fetchArticleCallback(final String wikiArticle, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer) {
        try {
            fetchArticleCompletableFuture(wikiArticle)
                    .thenAccept(articleConsumer);
        } catch (Exception e) {
            exceptionConsumer.accept(e);
        }
    }

    @Override
    public Future<String> fetchArticleFuture(final String wikiArticle) {
        return pool.submit(() -> wikiServiceJapi.getArticle(wikiArticle));
    }

    @Override
    public CompletableFuture<String> fetchArticleCompletableFuture(final String wikiArticle) {
        return CompletableFuture.supplyAsync(() -> wikiServiceJapi.getArticle(wikiArticle), pool);
    }

    //---------------------------------------------------------------------------------

    @Override
    public Flux<ParsedPage> parseMediaWikiTextFlux(String mediaWikiText) {
        return Flux.just(mediaWikiText).map(this::parseMediaWikiText);
    }

    @Override
    public Observable<ParsedPage> parseMediaWikiTextObservable(String mediaWikiText) {
        return Observable.fromPublisher(parseMediaWikiTextFlux(mediaWikiText));
    }

    @Override
    public ParsedPage parseMediaWikiText(String mediaWikiText) {
        return parser.parse(mediaWikiText);
    }


    @Override
    public Future<ParsedPage> parseMediaWikiTextFuture(String mediaWikiText) {
        return pool.submit(() -> parseMediaWikiText(mediaWikiText));
    }


    @Override
    public CompletableFuture<ParsedPage> parseMediaWikiTextCompletableFuture(String mediaWikiText) {
        return CompletableFuture.supplyAsync(() -> parseMediaWikiText(mediaWikiText), pool);
    }

    private static final List<String> WIKI_ARTICLES = Arrays.asList(
            "42", "Foo", "Korea", "Gaya", "Ostasien", "China", "Russland", "Gelbes_Meer",
            "Japanisches_Meer", "Observable", "Energie", "Zeitentwicklung", "Quantenmechanik",
            "Meter", "Kilogramm", "Lichtgeschwindigkeit", "Spin", "Wellenmechanik", "Erwin_Schrödinger",
            "Infinitesimalrechnung", "Joule", "Elektronenvolt", "Photon", "Teilchen", "Teilchen",
            "Astroide", "Pseudonorm", "Kompakter_Raum", "Isometrie", "Potenzmenge", "Vektorraum",
            "Mathematik", "Median", "Kumulante", "Indikatorfunktion", "Zufallsvariable", "Mittelwert",
            "Quantenphysik", "Komplexe_Zahl", "Biologie", "Wirtschaft", "Chemie", "Technik", "Physik");


    @Override
    public Flux<String> wikiArticleBeingReadFlux(final long interval, final TimeUnit unit) {
        return Flux.from(wikiArticleBeingReadObservable(interval, unit).toFlowable(MISSING));
    }

    @Override
    public Observable<String> wikiArticleBeingReadObservable(final long interval, final TimeUnit unit) {
        final Random randomGenerator = new Random(8L);
        PublishSubject<String> publishSubject = PublishSubject.create();
        Observable.interval(interval, unit)
                .map(time -> {
                    String article = WIKI_ARTICLES.get(randomGenerator.nextInt(WIKI_ARTICLES.size()));
                    print("wikiArticleBeingReadObservable=%s", article);
                    return article;
                }).subscribe(publishSubject);

        return publishSubject;
    }

    @Override
    public Flux<String> wikiArticleBeingReadFluxBurst() {
        return Flux.from(wikiArticleBeingReadObservableBurst().toFlowable(MISSING));
    }

    @Override
    public Observable<String> wikiArticleBeingReadObservableBurst() {
        final Random randomGenerator = new Random(4L);
        PublishSubject<String> publishSubject = PublishSubject.create();
        ReactiveUtil.burstSource()
                .map(ignore -> {
                    String article = WIKI_ARTICLES.get(randomGenerator.nextInt(WIKI_ARTICLES.size()));
                    System.out.println(getThreadId() + "wikiArticleBeingReadObservable=" + article);
                    return article;
                }).subscribe(publishSubject);
        return publishSubject;
    }

    @Override
    public Observable<String> wikiArticleBeingReadObservableWithRandomErrors() {
        final Random randomGenerator = new Random(12L);
        return wikiArticleBeingReadObservableBurst().map(article -> {
            if (randomGenerator.nextInt() % 20 == 0) {
                throw new IllegalStateException("something's wrong");
            }
            return article;
        });
    }
}
