package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.PersistService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import com.senacor.tecco.reactive.util.FlakinessFunction;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static java.lang.ClassLoader.getSystemResourceAsStream;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Andreas Keefer
 */
public class Kata9Backpressure {

    private final WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1),
            FlakinessFunction.noFlakiness(), true, "de");
    private final PersistService persistService = PersistService.create();
    private final Scheduler io = Schedulers.elastic();

    /**
     * run this with -Xmx64m
     */
    public static void main(String[] args) throws Exception {
        new Kata9Backpressure().backpressure();
    }

    private void backpressure() throws Exception {
        // 1. run the main method with -Xmx64m and recognize the OutOfMemoryError.
        // 2. change the readWikiArticlesFromFile to a producer which handles backpressure.
        // 3. change the consumer to signal the producer his needs/capacity.

        WaitMonitor monitor = new WaitMonitor();
        //String fileName = "articles.100.txt";
        //String fileName = "articles.1000.txt";
//        String fileName = "articles.10000.txt";
        String fileName = "articles.100000.txt";
        //String fileName = "articles.1000000.txt";

        final BaseSubscriber<String> subscriber;
        readWikiArticlesFromFile(fileName)
                .doOnNext(next -> print("reading article: %s", next))
                .flatMap(article -> wikiService.fetchArticleFlux(article).subscribeOn(io))
                .subscribeOn(io)
                .subscribe(subscriber = new BaseSubscriber<String>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(10);
                    }

                    @Override
                    protected void hookOnNext(String article) {
                        persistService.save(article);
                        request(1);
                    }

                    @Override
                    protected void hookOnComplete() {
                        print("complete!");
                        monitor.complete();
                    }

                    @Override
                    protected void hookOnError(Throwable t) {
                        t.printStackTrace();
                    }
                });

        monitor.waitFor(120, SECONDS);
        subscriber.dispose();
    }

    /**
     * read Articles from file
     *
     * @param fileName File name (e.g. "articles.100.txt")
     * @return stream of Article names from File (line-by-line)
     */
    private Flux<String> readWikiArticlesFromFile(String fileName) {
        return Flux.generate(
                () -> new BufferedReader(new InputStreamReader(getSystemResourceAsStream(fileName))),
                (bufferedReader, emitter) -> {
                    try {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            emitter.complete();
                        } else {
                            emitter.next(line);
                        }
                    } catch (IOException ex) {
                        emitter.error(ex);
                    }
                    return bufferedReader;
                },
                bufferedReader -> {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        );
    }
}