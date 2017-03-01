package com.senacor.tecco.reactive.services;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;
import static com.senacor.tecco.reactive.ReactiveUtil.getThreadId;

public class RatingService {

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public Observable<Integer> rateObservable(final ParsedPage parsedPage) {
        return Observable.just(parsedPage).map(this::rate);
    }

    public Flux<Integer> rateFlux(final ParsedPage parsedPage) {
        return Flux.just(parsedPage).map(this::rate);
    }

    public Future<Integer> rateFuture(ParsedPage parsedPage) {
        return pool.submit(() -> rate(parsedPage));
    }

    public CompletableFuture<Integer> rateCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> rate(parsedPage), pool);
    }

    public int rate(final ParsedPage parsedPage) {
        if (null == parsedPage) {
            throw new IllegalStateException("parsedPage must not be null");
        }
        long start = System.currentTimeMillis();
        int articleSize = parsedPage.getText().length();
        int linksCount = parsedPage.getLinks().size();

        fixedDelay(30);

        if (0 == linksCount) {
            // 0 sterne
            return 0;
        }

        final BigDecimal percent = determinePercent(articleSize, linksCount);

        final int rating = determineRating(percent);

        System.out.println(String.format("%srate: articleSize=%s linksCount=%s percent=%s runtime=%sms",
                getThreadId(), articleSize, linksCount, percent, System.currentTimeMillis() - start));
        return rating;
    }

    private BigDecimal determinePercent(int articleSize, int linksCount) {
        return BigDecimal.valueOf(linksCount)
                .divide(BigDecimal.valueOf(articleSize), 3, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private int determineRating(BigDecimal percent) {
        if (percent.compareTo(BigDecimal.valueOf(0.8)) > 0) {
            // 5 sterne
            return 5;
        } else if (percent.compareTo(BigDecimal.valueOf(0.5)) > 0) {
            // 4 sterne
            return 4;
        } else if (percent.compareTo(BigDecimal.valueOf(0.3)) > 0) {
            // 3 sterne
            return 3;
        } else if (percent.compareTo(BigDecimal.valueOf(0.1)) > 0) {
            // 2 sterne
            return 2;
        } else {
            // 1 stern
            return 1;
        }
    }
}
