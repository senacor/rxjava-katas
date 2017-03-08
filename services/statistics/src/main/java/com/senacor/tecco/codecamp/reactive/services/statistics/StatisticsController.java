package com.senacor.tecco.codecamp.reactive.services.statistics;

import com.senacor.tecco.codecamp.reactive.services.statistics.external.ArticleReadEventsService;
import com.senacor.tecco.codecamp.reactive.services.statistics.external.ArticleMetricsService;
import com.senacor.tecco.codecamp.reactive.services.statistics.model.ArticleMetrics;
import com.senacor.tecco.codecamp.reactive.services.statistics.external.ArticleReadEvent;
import com.senacor.tecco.codecamp.reactive.services.statistics.model.ArticleStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * @author Andri Bremm
 */
@RestController
public class StatisticsController {

    private ArticleReadEventsService articleReadEventsService;
    private ArticleMetricsService articleMetricsService;

    @Autowired
    public StatisticsController(ArticleReadEventsService articleReadEventsService, ArticleMetricsService articleMetricsService) {
        this.articleReadEventsService = articleReadEventsService;
        this.articleMetricsService = articleMetricsService;
    }

    @GetMapping(value = "/statistics/article", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ArticleStatistics> fetchArticleStatistics(@RequestParam(required = false, defaultValue = "1000") int updateInterval) {
        Flux<ArticleReadEvent> events = articleReadEventsService.readEvents();

        return events
                .map(articleReadEvent -> articleReadEvent.getArticleName())
                .flatMap(articleName -> Flux.zip(
                        articleMetricsService.fetchRating(articleName),
                        articleMetricsService.fetchWordCount(articleName),
                        (rating, wordCount) -> new ArticleMetrics(rating, wordCount)))
                .bufferMillis(updateInterval)
                .map(StatisticsController::calculateArticleStatistics)
                .log();
    }

    private static ArticleStatistics calculateArticleStatistics(List<ArticleMetrics> articleMetricss) {
        long wordCountSum = 0;
        long ratingSum = 0;
        for (ArticleMetrics metrics : articleMetricss) {
            wordCountSum += metrics.getWordCount();
            ratingSum += metrics.getRating();
        }
        Integer numOfArticles = articleMetricss.size();
        double wordCountAvg = wordCountSum / numOfArticles.doubleValue();
        double ratingAvg = ratingSum / numOfArticles.doubleValue();
        return new ArticleStatistics(numOfArticles, wordCountAvg, ratingAvg);
    }

}
