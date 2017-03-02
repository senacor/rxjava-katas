package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import org.apache.commons.lang3.Validate;
import reactor.core.publisher.Flux;

import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

public class CountServiceImpl implements CountService {

    CountServiceImpl() {
    }

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    @Override
    public Observable<Integer> countWordsObservable(final ParsedPage parsedPage) {
        return Observable.just(parsedPage).map(this::countWords);
    }

    @Override
    public Flux<Integer> countWordsFlux(final ParsedPage parsedPage) {
        return Flux.just(parsedPage).map(this::countWords);
    }

    @Override
    public Future<Integer> countWordsFuture(final ParsedPage parsedPage) {
        return pool.submit(() -> countWords(parsedPage));
    }


    @Override
    public CompletableFuture<Integer> countWordsCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> countWords(parsedPage), pool);
    }

    @Override
    public int countWords(final ParsedPage parsedPage) {
        Validate.notNull(parsedPage, "parsedPage must not be null");
        String text = parsedPage.getText();
        int wordCount = new StringTokenizer(text, " ").countTokens();
        print("countWords: %s", wordCount);
        return wordCount;
    }
}
