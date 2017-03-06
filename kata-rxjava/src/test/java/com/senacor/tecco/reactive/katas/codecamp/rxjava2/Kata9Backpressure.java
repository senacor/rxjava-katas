package com.senacor.tecco.reactive.katas.codecamp.rxjava2;

import com.senacor.tecco.reactive.katas.KataClassification;
import com.senacor.tecco.reactive.services.PersistService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import com.senacor.tecco.reactive.util.FlakinessFunction;
import com.senacor.tecco.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.senacor.tecco.reactive.katas.KataClassification.Classification.advanced;
import static com.senacor.tecco.reactive.katas.KataClassification.Classification.mandatory;
import static com.senacor.tecco.reactive.util.ReactiveUtil.print;
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

    @KataClassification({mandatory, advanced})
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

        Disposable subscriber = readWikiArticlesFromFile(fileName)
                .doOnNext(next -> print("reading article: %s", next))
                .flatMap(article -> wikiService.fetchArticleObservable(article)
                        .subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .subscribe(persistService::save,
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
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
    private Observable<String> readWikiArticlesFromFile(String fileName) {
        return Observable.create(emitter -> {
            BufferedReader bufferedReader;
            try {
                bufferedReader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));
            } catch (IOException | URISyntaxException e) {
                emitter.onError(e);
                return;
            }
            try {
                String line = bufferedReader.readLine();
                while (line != null && !emitter.isDisposed()) {
                    emitter.onNext(line);
                    line = bufferedReader.readLine();
                }
                emitter.onComplete();
            } catch (IOException ex) {
                emitter.onError(ex);
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}