package com.senacor.tecco.codecamp.reactive.services.statistics;

import com.senacor.tecco.codecamp.reactive.services.statistics.external.ArticleMetricsService;
import com.senacor.tecco.codecamp.reactive.services.statistics.external.ArticleReadEventsService;
import com.senacor.tecco.codecamp.reactive.services.statistics.model.ArticleStatistics;
import com.senacor.tecco.codecamp.reactive.services.statistics.model.TopArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author Andri Bremm
 */
@RestController
public class StatisticsController {

    private final ArticleReadEventsService articleReadEventsService;
    private final ArticleMetricsService articleMetricsService;

    @Autowired
    public StatisticsController(ArticleReadEventsService articleReadEventsService,
                                ArticleMetricsService articleMetricsService) {
        this.articleReadEventsService = articleReadEventsService;
        this.articleMetricsService = articleMetricsService;
    }

    @GetMapping("/top/article")
    public Flux<List<TopArticle>> topArticle(@RequestParam(required = false, defaultValue = "1000") int updateInterval,
                                             @RequestParam(required = false, defaultValue = "5") int numberOfTopArticles) {
        return Flux.empty();
    }

    @GetMapping("/statistics/article")
    public Flux<ArticleStatistics> fetchArticleStatistics(@RequestParam(required = false, defaultValue = "1000") int updateInterval) {
        return Flux.empty();
    }
}
