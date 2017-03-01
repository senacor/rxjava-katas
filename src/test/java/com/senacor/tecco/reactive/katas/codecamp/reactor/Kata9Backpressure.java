package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import org.apache.commons.lang3.StringUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Andreas Keefer
 */
public class Kata9Backpressure {

    private final WikiService wikiService = new WikiService(true, 1, false);
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
//        String fileName = "articles.1000000.txt";

        Disposable subscriber = readWikiArticlesFromFile(fileName)
                .flatMap(article -> wikiService.fetchArticleFlux(article).subscribeOn(io))
                .subscribeOn(io)
//                .log()
                .subscribe(this::write,
                        Throwable::printStackTrace,
                        monitor::complete);

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
        return Flux.create(emitter -> {
            InputStream file = ClassLoader.getSystemResourceAsStream(fileName);
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file))) {
                String line = bufferedReader.readLine();
                while (line != null && !emitter.isCancelled()) {
                    emitter.next(line);
                    line = bufferedReader.readLine();
                }
                emitter.complete();
            } catch (IOException ex) {
                emitter.error(ex);
            }
        });
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