package com.senacor.tecco.services.readarticles.aggregation;

import com.senacor.tecco.services.readarticles.external.MetricsService;
import com.senacor.tecco.services.readarticles.external.WikiLoaderService;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class AggregationEndpoint {

    private final MetricsService metricsService;
    private final WikiLoaderService wikiLoaderService;
    private final Flux<ReadStatistics> readArticle;

    public AggregationEndpoint(Flux<String> articleNames, MetricsService metricsService, WikiLoaderService wikiLoaderService) {
        this.metricsService = metricsService;
        this.wikiLoaderService = wikiLoaderService;

        DirectProcessor<ReadStatistics> processor = DirectProcessor.create();
        aggregateReadArticle(articleNames).subscribe(processor);
        readArticle = processor;
    }

    public Flux<ReadStatistics> aggregateReadArticle(Flux<String> articleNames) {
        return articleNames.log().flatMap(wikiLoaderService::fetchArticle)
                           .flatMap(article -> {
                                       Flux<Integer> rating = metricsService.fetchRating(article.getContent());
                                       Flux<Integer> wordCount = metricsService.fetchWordcount(article.getContent());
                                       return rating.zipWith(wordCount,
                                               (r, wc) -> new ReadStatistics(article.getName(), wc, r)
                                       );
                                   }
                           );
    }

    @GetMapping("/read")
    public Flux<ReadStatistics> getReadArticle() {
        return readArticle;
    }

}
