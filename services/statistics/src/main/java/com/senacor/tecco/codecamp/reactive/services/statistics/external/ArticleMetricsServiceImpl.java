package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import com.senacor.tecco.codecamp.reactive.services.statistics.WrongStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.senacor.tecco.codecamp.reactive.services.statistics.external.URLEncoderUtil.urlEncode;

/**
 * @author Daniel Heinrich
 */
@Service
public class ArticleMetricsServiceImpl implements ArticleMetricsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleMetricsServiceImpl.class);

    public static final String ARTICLE = "article";
    public static final String WORD_COUNT = "wordcount";
    public static final String RATING = "rating";

    private final WebClient articleClient;

    @Autowired
    public ArticleMetricsServiceImpl(WebClient articleClient) {
        this.articleClient = articleClient;
    }

    @Override
    public Mono<Integer> fetchWordCount(String articleName) {
        return articleClient.get()
                            .uri(ub -> ub.pathSegment(ARTICLE, urlEncode(articleName), WORD_COUNT).build())
                            .exchange()
                            .doOnError(e -> LOGGER.error(e.getMessage()))
                            .retry(1)
                            .doOnNext(WrongStatusException.okFilter())
                            .flatMap(r -> r.bodyToMono(String.class))
                            .map(Integer::parseInt).single();
    }

    @Override
    public Mono<Integer> fetchRating(String articleName) {
        return articleClient.get()
                            .uri(ub -> ub.pathSegment(ARTICLE, urlEncode(articleName), RATING).build())
                            .exchange()
                            .doOnError(e -> LOGGER.error(e.getMessage()))
                            .retry(1)
                            .doOnNext(WrongStatusException.okFilter())
                            .flatMap(r -> r.bodyToMono(String.class))
                            .map(Integer::parseInt).single();
    }
}
