package com.senacor.codecamp.reactive.services.statistics;

import com.senacor.codecamp.reactive.services.statistics.external.ArticleMetricsService;
import com.senacor.codecamp.reactive.services.statistics.external.ArticleReadEvent;
import com.senacor.codecamp.reactive.services.statistics.external.ArticleReadEventsService;
import com.senacor.codecamp.reactive.services.statistics.external.URLEncoderUtil;
import com.senacor.codecamp.reactive.services.statistics.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.time.Duration.ofMillis;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * @author Andri Bremm
 */
@RestController
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Value("${services.article.base-url}")
    private String articleBaseUri;

    private ArticleReadEventsService articleReadEventsService;
    private ArticleMetricsService articleMetricsService;

    @Autowired
    public StatisticsController(ArticleReadEventsService articleReadEventsService, ArticleMetricsService articleMetricsService) {
        this.articleReadEventsService = articleReadEventsService;
        this.articleMetricsService = articleMetricsService;
    }

    private static final Map<String, Long> readStatistics = new ConcurrentHashMap<>();

    @GetMapping(value = "/top/article", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<List<TopArticle>> topArticle(@RequestParam(required = false, defaultValue = "1000") int updateInterval,
                                             @RequestParam(required = false, defaultValue = "5") int numberOfTopArticles) {
        return articleReadEventsService.readEvents()
                .doOnNext(StatisticsController::updateReadStatistic)
                .sample(ofMillis(updateInterval))
                .map(readEvent -> createTopArticleList(numberOfTopArticles))
                .retry(throwable -> {
                    logger.warn("error on topArticle, retrying", throwable);
                    return true;
                })
                .log();
    }

    @GetMapping(value = "/statistics/article", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ArticleStatistics> fetchArticleStatistics(@RequestParam(required = false, defaultValue = "1000") int updateInterval) {
        return articleReadEventsService.readEvents()
                .onBackpressureDrop(articleReadEvent -> logger.warn("dropping articleReadEvent: " + articleReadEvent))
                .buffer(ofMillis(updateInterval / 3))
                .filter(articleReadEvents -> !articleReadEvents.isEmpty())
                .flatMap(articleReadEvent -> {
                    Flux<ArticleName> distinctArticleNames = Flux.fromIterable(articleReadEvent)
                                                                 .map(ArticleReadEvent::toArticleName)
                                                                 .distinct()
                                                                 .cache();
                    Flux<Map<String, Integer>> rating = articleMetricsService.fetchRatings(distinctArticleNames)
                            .collectMap(Rating::getArticleName, Rating::getRating)
                            .cache()
                            .repeat(articleReadEvent.size());
                    Flux<Map<String, Integer>> wordCount = articleMetricsService.fetchWordCounts(distinctArticleNames)
                            .collectMap(WordCount::getArticleName, WordCount::getCount)
                            .cache()
                            .repeat(articleReadEvent.size());
                    return Flux.zip(Flux.fromIterable(articleReadEvent), rating, wordCount)
                            .map(zip -> new ArticleMetrics(zip.getT2().get(zip.getT1().getArticleName()),
                                    zip.getT3().get(zip.getT1().getArticleName()),
                                    zip.getT1().getFetchTimeInMillis()));
                })
                .buffer(ofMillis(updateInterval))
                .map(StatisticsController::calculateArticleStatistics)
                .retry(throwable -> {
                    logger.warn("error on fetchArticleStatistics, retrying", throwable);
                    return true;
                })
                .onBackpressureDrop(articleStatistics -> logger.warn("dropping articleStatistics: " + articleStatistics))
                .log();
    }

    private List<TopArticle> createTopArticleList(int numberOfTopArticles) {
        return readStatistics.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue).reversed())
                .limit(numberOfTopArticles)
                .map(entry -> {
                    String articleUrl = articleBaseUri + "/article/" + URLEncoderUtil.urlEncode(entry.getKey());
                    return new TopArticle(entry.getKey(), articleUrl, entry.getValue());
                })
                .collect(Collectors.toList());
    }

    private static void updateReadStatistic(ArticleReadEvent readEvent) {
        long reads = 1l;
        String articleName = readEvent.getArticleName();
        if (readStatistics.containsKey(articleName)) {
            reads = readStatistics.get(articleName) + 1;
        }
        readStatistics.put(articleName, reads);
    }

    private static ArticleStatistics calculateArticleStatistics(List<ArticleMetrics> articleMetricss) {
        long wordCountSum = 0;
        long ratingSum = 0;
        long fetchTimeInMillisSum = 0;
        for (ArticleMetrics metrics : articleMetricss) {
            wordCountSum += metrics.getWordCount();
            ratingSum += metrics.getRating();
            fetchTimeInMillisSum += metrics.getFetchTimeInMillis() == null ? 0 : metrics.getFetchTimeInMillis();
        }
        Integer numOfArticles = articleMetricss.size();
        double wordCountAvg = wordCountSum / numOfArticles.doubleValue();
        double ratingAvg = ratingSum / numOfArticles.doubleValue();
        double fetchTimeInMillisAvg = fetchTimeInMillisSum / numOfArticles.doubleValue();
        return new ArticleStatistics(numOfArticles, wordCountAvg, ratingAvg, fetchTimeInMillisAvg);
    }

}
