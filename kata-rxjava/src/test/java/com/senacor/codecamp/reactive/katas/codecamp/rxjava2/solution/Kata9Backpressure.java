package com.senacor.codecamp.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.codecamp.reactive.services.PersistService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.Flowable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Andreas Keefer
 */
public class Kata9Backpressure {

    private final WikiService wikiService = WikiService.create(DelayFunction.withNoDelay(),
            FlakinessFunction.noFlakiness(), true, "de");
    private final PersistService persistService = PersistService.create();

    /**
     * run this with -Xmx64m
     */
    public static void main(String[] args) throws Exception {
        RxJavaPlugins.setErrorHandler(error -> {
            error.printStackTrace();
            System.exit(1);
        });
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

        final DisposableSubscriber<String> subscriber;
        readWikiArticlesFromFile(fileName)
                .doOnNext(next -> print("reading article: %s", next))
                .flatMap(article -> wikiService.fetchArticleFlowable(article)
                        .subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber = new DisposableSubscriber<String>() {
                    @Override
                    protected void onStart() {
                        request(10);
                    }

                    @Override
                    public void onNext(String article) {
                        persistService.save(article);
                        request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        print("complete!");
                        monitor.complete();
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
    private Flowable<String> readWikiArticlesFromFile(String fileName) {
        return Flowable.generate(
                () -> Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))
                , (bufferedReader, emitter) -> {
                    try {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            emitter.onComplete();
                        } else {
                            emitter.onNext(line);
                        }
                    } catch (IOException ex) {
                        emitter.onError(ex);
                    }
                }
                , reader -> {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        );
    }
}