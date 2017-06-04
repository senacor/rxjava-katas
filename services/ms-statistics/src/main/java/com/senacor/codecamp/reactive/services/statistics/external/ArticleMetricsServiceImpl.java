package com.senacor.codecamp.reactive.services.statistics.external;

import com.senacor.codecamp.reactive.services.statistics.WrongStatusException;
import com.senacor.codecamp.reactive.services.statistics.model.ArticleName;
import com.senacor.codecamp.reactive.services.statistics.model.Rating;
import com.senacor.codecamp.reactive.services.statistics.model.WordCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Daniel Heinrich
 */
@Service
public class ArticleMetricsServiceImpl implements ArticleMetricsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleMetricsServiceImpl.class);

    public static final String ARTICLE = "article";
    public static final String WORD_COUNT = "wordcount";
    public static final String RATING = "rating";
    public static final String WORD_COUNTS = "wordcounts";
    public static final String RATINGS = "ratings";
    public static final MediaType APPLICATION_STREAM_JSON_UTF8 = MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8");

    private final WebClient wikiServiceClient;

    @Autowired
    public ArticleMetricsServiceImpl(WebClient wikiServiceClient) {
        this.wikiServiceClient = wikiServiceClient;
    }

    @Override
    public Mono<Integer> fetchWordCount(String articleName) {
        return wikiServiceClient.get()
                .uri(ub -> ub.pathSegment(ARTICLE, URLEncoderUtil.urlEncode(articleName), WORD_COUNT).build())
                .exchange()
                .doOnError(e -> LOGGER.error(e.getMessage()))
                .retry(1)
                .doOnNext(WrongStatusException.okFilter())
                .flatMap(r -> r.bodyToMono(String.class))
                .map(Integer::parseInt)
                .publishOn(Schedulers.single());
    }

    @Override
    public Mono<Integer> fetchRating(String articleName) {
        return wikiServiceClient.get()
                .uri(ub -> ub.pathSegment(ARTICLE, URLEncoderUtil.urlEncode(articleName), RATING).build())
                .exchange()
                .doOnError(e -> LOGGER.error(e.getMessage()))
                .retry(1)
                .doOnNext(WrongStatusException.okFilter())
                .flatMap(r -> r.bodyToMono(String.class))
                .map(Integer::parseInt)
                .publishOn(Schedulers.single());
    }

    @Override
    public Flux<WordCount> fetchWordCounts(Flux<ArticleName> articleNames) {
        return wikiServiceClient.post()
                .uri(ub -> ub.pathSegment(ARTICLE, WORD_COUNTS).build())
                .accept(APPLICATION_STREAM_JSON_UTF8)
                .body(articleNames, ArticleName.class)
                .exchange()
                .doOnError(e -> LOGGER.error(e.getMessage()))
                .retry(1)
                .doOnNext(WrongStatusException.okFilter())
                .flatMapMany(r -> r.bodyToFlux(WordCount.class));
    }

    @Override
    public Flux<Rating> fetchRatings(Flux<ArticleName> articleNames) {
        return wikiServiceClient.post()
                .uri(ub -> ub.pathSegment(ARTICLE, RATINGS).build())
                .accept(APPLICATION_STREAM_JSON_UTF8)
                .body(articleNames, ArticleName.class)
                .exchange()
                .doOnError(e -> LOGGER.error(e.getMessage()))
                .retry(1)
                .doOnNext(WrongStatusException.okFilter())
                .flatMapMany(r -> r.bodyToFlux(Rating.class));
    }
}
