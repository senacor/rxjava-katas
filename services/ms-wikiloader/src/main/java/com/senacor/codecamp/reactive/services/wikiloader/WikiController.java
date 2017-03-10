package com.senacor.codecamp.reactive.services.wikiloader;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Stopwatch;
import com.senacor.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.codecamp.reactive.services.wikiloader.model.ArticleName;
import com.senacor.codecamp.reactive.services.wikiloader.model.Rating;
import com.senacor.codecamp.reactive.services.wikiloader.model.WordCount;
import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.services.wikiloader.service.ArticleService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofMillis;

/**
 * @author Andreas Keefer
 */
@RestController
@RequestMapping("/article")
public class WikiController {

    public static final int BUFFER_READ_EVENTS = 250;

    private final ArticleService articleService;
    private final DirectProcessor<Article> readArticles = DirectProcessor.create();

    @Autowired
    public WikiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * This endpoint fetches an wikipedia article by name as media wiki text.
     *
     * @param name article name
     * @return article with media wiki as content
     */
    @GetMapping("/{name}")
    public Mono<Article> fetchArticle(@PathVariable final String name) {
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        return articleService.fetchArticle(name)
                .doOnSubscribe(subscription -> stopwatch.start())
                .map(content -> Article.newBuilder()
                        .withName(name)
                        .withContent(content)
                        .withFetchTimeInMillis((int) stopwatch.stop().elapsed(TimeUnit.MILLISECONDS))
                        .build())
                .doOnNext(readArticles::onNext)
                .log();
    }

    /**
     * This endpoint streams an event for each article name, which is fetched by {@link #fetchArticle}.
     * This is a HOT Source (infinite stream) and acts as a Publisher in a publish/subscribe scenario.
     */
    @CrossOrigin
    @GetMapping("/readevents")
    @JsonView(Article.NameOnly.class)
    public Flux<List<Article>> getReadStream() {
        return readArticles
                .buffer(ofMillis(BUFFER_READ_EVENTS))
                .filter(list -> !list.isEmpty())
                .log();
    }

    @GetMapping("/{name}/wordcount")
    public Mono<Integer> getWordCount(@PathVariable String name) {
        return articleService.countWords(name)
                .log();
    }

    @RequestMapping("/wordcounts")
    public Flux<WordCount> countWords(@RequestBody Flux<ArticleName> names) {
        return names.flatMap(articleName -> articleService.countWords(articleName.getName())
                .map(count -> new WordCount(articleName.getName(), count)))
                .log();
    }

    @GetMapping("/{name}/rating")
    public Mono<Integer> getRating(@PathVariable String name) {
        return articleService.rate(name)
                .log();
    }

    @RequestMapping("/ratings")
    public Flux<Rating> ratings(@RequestBody Flux<ArticleName> names) {
        return names.flatMap(articleName -> articleService.rate(articleName.getName())
                .map(rating -> new Rating(articleName.getName(), rating)))
                .log();
    }
}
