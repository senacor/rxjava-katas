package com.senacor.tecco.services.aggregation;

import com.senacor.tecco.reactive.services.WikiService;
import io.reactivex.processors.PublishProcessor;
import org.reactivestreams.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
@RestController
public class AggregationEndpoint {

    private final MetricsService metricsService;
    private final WikiLoaderService wikiLoaderService;
    private final Flux<ReadStatistics> readArticle;

    @Autowired
    public AggregationEndpoint(MetricsService metricsService, WikiLoaderService wikiLoaderService) {
        this.metricsService = metricsService;
        this.wikiLoaderService = wikiLoaderService;

        DirectProcessor<ReadStatistics> processor = DirectProcessor.create();
        Flux<String> articles = WikiService.create()
                                             .wikiArticleBeingReadFluxBurstOwn()
                                             .onBackpressureDrop();
//                .delayElements(Duration.ofSeconds(1));
        aggregateReadArticle(articles).subscribe(processor);
        readArticle = processor;
    }

    public Flux<ReadStatistics> aggregateReadArticle(Flux<String> articleNames) {
        return articleNames.log().flatMap(wikiLoaderService::fetchArticle)
                           .flatMap(article -> {
                                       Flux<Integer> rating = metricsService.fetchRating(article.getText());
                                       Flux<Integer> wordCount = metricsService.fetchWordcount(article.getText());
                                       return rating.zipWith(wordCount,
                                               (r, wc) -> new ReadStatistics(article.getName(), wc, r)
                                       );
                                   }
                           );
    }

    @GetMapping("/read")
    public Flux<ReadStatistics> getReadArticle() {
        return readArticle.take(1);
    }

}
