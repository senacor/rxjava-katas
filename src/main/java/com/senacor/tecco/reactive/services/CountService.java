package com.senacor.tecco.reactive.services;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import rx.Observable;

import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;
import static com.senacor.tecco.reactive.ReactiveUtil.getThreadId;

public class CountService {

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public Observable<Integer> countWords(final ParsedPage parsedPage) {
        return Observable.create(subscriber -> {
            if (null == parsedPage) {
                subscriber.onError(new IllegalStateException("parsedPage must not be null"));
                return;
            }
            subscriber.onNext(countWordsSynchronous(parsedPage));
            subscriber.onCompleted();
        });
    }

    public Future<Integer> countWordsFuture(final ParsedPage parsedPage) {
        return pool.submit(() -> countWordsSynchronous(parsedPage));
    }


    public CompletableFuture<Integer> countWordsCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> countWordsSynchronous(parsedPage), pool);
    }

    public int countWordsSynchronous(final ParsedPage parsedPage) {
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
