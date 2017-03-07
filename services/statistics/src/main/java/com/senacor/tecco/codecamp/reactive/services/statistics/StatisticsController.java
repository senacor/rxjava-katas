package com.senacor.tecco.codecamp.reactive.services.statistics;

import com.senacor.tecco.codecamp.reactive.services.statistics.model.ArticleStatistics;
import com.senacor.tecco.codecamp.reactive.services.statistics.model.ReadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * @author Andri Bremm
 */
@RestController
public class StatisticsController {

    private ReadEventsService readEventsService;

    @Autowired
    public StatisticsController(ReadEventsService readEventsService) {
        this.readEventsService = readEventsService;
    }

    @GetMapping(value = "/statistics/article", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ArticleStatistics> fetchArticleStatistics(@RequestParam(required = false, defaultValue = "1000") int updateInterval) {
        Flux<ReadEvent> events = readEventsService.readEvents();

        return events.bufferMillis(updateInterval).map(readEvents -> {
            long wordCountSum = 0;
            long ratingSum = 0;
            for (ReadEvent readEvent : readEvents) {
                wordCountSum += readEvent.getWordCount();
                ratingSum += readEvent.getRating();
            }
            Integer numOfArticles = readEvents.size();
            double wordCountAvg = wordCountSum / numOfArticles.doubleValue();
            double ratingAvg = ratingSum / numOfArticles.doubleValue();
            return new ArticleStatistics(numOfArticles, wordCountAvg, ratingAvg);
        });
    }

}
