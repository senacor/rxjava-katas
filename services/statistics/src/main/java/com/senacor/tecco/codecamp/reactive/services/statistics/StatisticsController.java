package com.senacor.tecco.codecamp.reactive.services.statistics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Andri Bremm
 */
@RestController
public class StatisticsController {

    @GetMapping("/statistics/article")
    public Flux<ArticleStatistics> fetchArticleStatistics(@RequestParam(required = false, defaultValue = "1000") int updateInterval) {

        // TODO: consume "being read" service to create the statistics.

        return Flux.intervalMillis(updateInterval).map(number -> new ArticleStatistics(number));
    }

    private class ArticleStatistics {
        private long articleCount;
        private double wordCountAvg;
        private double ratingAvg;

        public ArticleStatistics(Long number) {
            this.articleCount = number;
            this.wordCountAvg = 123.12;
            this.ratingAvg = 3.7;
        }

        public long getArticleCount() {
            return articleCount;
        }

        public double getWordCountAvg() {
            return wordCountAvg;
        }

        public double getRatingAvg() {
            return ratingAvg;
        }
    }
}
