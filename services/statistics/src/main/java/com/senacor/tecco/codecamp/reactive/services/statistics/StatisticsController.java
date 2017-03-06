package com.senacor.tecco.codecamp.reactive.services.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

/**
 * @author Andri Bremm
 */
@RestController
public class StatisticsController {

    @Value("${services.read-articles-aggregation}")
    private String uri;

    private WebClient webClient;

    @Autowired
    public StatisticsController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/statistics/article")
    public Flux<ArticleStatistics> fetchArticleStatistics(@RequestParam(required = false, defaultValue = "1000") int updateInterval) {
        Flux<ReadEvent> result = webClient.get()
                .uri(URI.create(uri))
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange().flatMap(response -> response.bodyToFlux(ReadEvent.class));

        return result.bufferMillis(updateInterval).map(readEvents -> {
            long wordCountSum = 0;
            long ratingSum = 0;
            for (ReadEvent readEvent : readEvents) {
                wordCountSum += readEvent.getWordCount();
                ratingSum += readEvent.getRating();
            }
            int numOfArticles = readEvents.size();
            return new ArticleStatistics(numOfArticles, wordCountSum / numOfArticles, ratingSum / numOfArticles);
        });
    }

    private class ArticleStatistics {
        private int articleCount;
        private double wordCountAvg;
        private double ratingAvg;

        public ArticleStatistics(int articleCount, double wordCountAvg, double ratingAvg) {
            this.articleCount = articleCount;
            this.wordCountAvg = wordCountAvg;
            this.ratingAvg = ratingAvg;
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

    public static class ReadEvent {
        private String articleName;
        private int wordCount;
        private int rating;

        public String getArticleName() {
            return articleName;
        }

        public void setArticleName(String articleName) {
            this.articleName = articleName;
        }

        public int getWordCount() {
            return wordCount;
        }

        public void setWordCount(int wordCount) {
            this.wordCount = wordCount;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }
    }
}
