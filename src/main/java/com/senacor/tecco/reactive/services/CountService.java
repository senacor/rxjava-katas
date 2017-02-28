package com.senacor.tecco.reactive.services;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;
import static com.senacor.tecco.reactive.ReactiveUtil.getThreadId;

public class CountService {

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public Observable<Integer> countWordsObservable(final ParsedPage parsedPage) {
        return Observable.just(parsedPage).map(this::countWords);
    }

    public Flux<Integer> countWordsFlux(final ParsedPage parsedPage) {
        return Flux.just(parsedPage).map(this::countWords);
    }

    public Future<Integer> countWordsFuture(final ParsedPage parsedPage) {
        return pool.submit(() -> countWords(parsedPage));
    }


    public CompletableFuture<Integer> countWordsCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> countWords(parsedPage), pool);
    }

    public int countWords(final ParsedPage parsedPage) {
        long start = System.currentTimeMillis();
        if (null == parsedPage) {
            throw new IllegalStateException("parsedPage must not be null");
        }
        String text = parsedPage.getText();
        fixedDelay(30);
        int wordCount = new StringTokenizer(text, " ").countTokens();
        System.out.println(String.format("%scountWords: count=%s runtime=%sms",
                getThreadId(), wordCount, System.currentTimeMillis() - start));
        return wordCount;
    }
}
