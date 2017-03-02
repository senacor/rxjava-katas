package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.*;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RatingServiceImpl implements RatingService {

    RatingServiceImpl() {
    }

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    @Override
    public Observable<Integer> rateObservable(final ParsedPage parsedPage) {
        return Observable.just(parsedPage).map(this::rate);
    }

    @Override
    public Flux<Integer> rateFlux(final ParsedPage parsedPage) {
        return Flux.just(parsedPage).map(this::rate);
    }

    @Override
    public Future<Integer> rateFuture(ParsedPage parsedPage) {
        return pool.submit(() -> rate(parsedPage));
    }

    @Override
    public CompletableFuture<Integer> rateCompletableFuture(ParsedPage parsedPage) {
        return CompletableFuture.supplyAsync(() -> rate(parsedPage), pool);
    }

    @Override
    public int rate(final ParsedPage parsedPage) {
        if (null == parsedPage) {
            throw new IllegalStateException("parsedPage must not be null");
        }
        int articleSize = parsedPage.getText().length();
        int linksCount = parsedPage.getLinks().size();

        if (0 == linksCount) {
            // 0 sterne
            return 0;
        }

        final BigDecimal percent = determinePercent(articleSize, linksCount);

        final int rating = determineRating(percent);

//        print("rate: articleSize=%s linksCount=%s percent=%s",
//                articleSize, linksCount, percent);
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
