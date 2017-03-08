package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Stopwatch;
import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article.NameOnly;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
@RestController
@RequestMapping("/article")
public class WikiController {

    private final DirectProcessor<Article> readArticles = DirectProcessor.create();

    private final WikiService wikiService;
    private final CountService countService;
    private final RatingService ratingService;
    private final PublisherCache<String, Article> cache;

    @Autowired
    public WikiController(WikiService wikiService, CountService countService, RatingService ratingService,
                          @Value("${article.cache.size}") int cacheSize) {
        this.wikiService = wikiService;
        this.countService = countService;
        this.ratingService = ratingService;
        this.cache = new PublisherCache<>(this::getArticle, cacheSize);
    }

    @GetMapping("/{name}")
    public Mono<Article> fetchArticle(@PathVariable String name) {
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        return cache.lookup(name)
                .doOnSubscribe(subscription -> stopwatch.start())
                .map(article -> Article.newBuilder(article)
                        .withFetchTimeInMillis((int) stopwatch.stop().elapsed(TimeUnit.MILLISECONDS))
                        .build())
                .doOnNext(readArticles::onNext);
    }

    private Mono<Article> getArticle(String name) {
        return wikiService.fetchArticleNonBlocking(name)
                .map(content -> Article.newBuilder().withName(name).withContent(content).build());
    }

    @GetMapping("/readevents")
    @JsonView(NameOnly.class)
    public Flux<Article> getReadStream() {
        return readArticles;
    }

    private Mono<ParsedPage> getParsedArticle(String name) {
        return cache.lookup(name)
                .map(Article::getContent)
                .map(wikiService::parseMediaWikiText);
    }

    @GetMapping("/{name}/wordcount")
    public Mono<Integer> getWordCount(@PathVariable String name) {
        return getParsedArticle(name)
                .map(countService::countWords);
    }

    @GetMapping("/{name}/rating")
    public Mono<Integer> getRating(@PathVariable String name) {
        return getParsedArticle(name)
                .map(ratingService::rate);
    }
}
