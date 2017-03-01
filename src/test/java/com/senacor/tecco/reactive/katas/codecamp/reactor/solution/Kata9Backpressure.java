package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.DisposableSubscriber;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static java.util.concurrent.TimeUnit.SECONDS;
import static reactor.core.scheduler.Schedulers.elastic;

/**
 * @author Andreas Keefer
 */
public class Kata9Backpressure {

    private final WikiService wikiService = new WikiService(true, 1, false);

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
        String fileName = "articles.10000.txt";
        //String fileName = "articles.100000.txt";
        //String fileName = "articles.1000000.txt";

        final BaseSubscriber<String> subscriber;
        readWikiArticlesFromFile(fileName)
                .doOnNext(next -> print("reading article: %s", next))
                .flatMap(article -> wikiService.fetchArticleFlux(article)
                        .subscribeOn(elastic()))
                .subscribeOn(elastic())
                .subscribe(subscriber = new BaseSubscriber<String>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(10);
                    }

                    @Override
                    protected void hookOnNext(String article) {
                        write(article);
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
                () -> Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))
                , (bufferedReader, emitter) -> {
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
                }
                , bufferedReader -> {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        );
    }

    private void write(String article) {
        // simulate some write operation
        print("write: " + StringUtils.abbreviate(article, 50).replaceAll("\\r\\n|\\r|\\n", " "));
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}