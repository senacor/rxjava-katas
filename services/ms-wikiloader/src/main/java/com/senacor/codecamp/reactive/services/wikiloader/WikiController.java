package com.senacor.codecamp.reactive.services.wikiloader;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.senacor.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.codecamp.reactive.services.wikiloader.model.ArticleName;
import com.senacor.codecamp.reactive.services.wikiloader.model.Rating;
import com.senacor.codecamp.reactive.services.wikiloader.model.WordCount;
import com.senacor.codecamp.reactive.services.wikiloader.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public Article fetchArticle(@PathVariable final String name) {
        // TODO Sprint 1
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        stopwatch.start();
        String articleContent = articleService.fetchArticleNonReactive(name);
        return Article.newBuilder().withName(name)
                .withContent(articleContent)
                .withFetchTimeInMillis((int) stopwatch.elapsed(TimeUnit.MILLISECONDS))
                .build();
    }

    /**
     * This endpoint streams an event for each article name, which is fetched by {@link #fetchArticle}.
     * This is a HOT Source (infinite stream) and acts as a Publisher in a publish/subscribe scenario.
     */
    @CrossOrigin
    @GetMapping("/readevents")
    @JsonView(Article.NameOnly.class)
    public List<Article> getReadStream() {
        // TODO Sprint2:
        return Lists.newArrayList(Article.newBuilder().withName("42").build());
    }

    @GetMapping("/{name}/wordcount")
    public Mono<Integer> getWordCount(@PathVariable String name) {
        return articleService.countWords(name)
                .log();
    }

    @RequestMapping("/wordcounts")
    public Flux<WordCount> countWords(@RequestBody Flux<ArticleName> names) {
        // TODO Sprint3
        List<WordCount> counts = names.toStream()
                .map(articleName -> {
                    System.out.println("count words for " + articleName.getName());
                    Integer count = articleService.countWords(articleName.getName()).block();
                    System.out.println("count = " + count);
                    return new WordCount(articleName.getName(), count);
                })
                .collect(Collectors.toList());
        return Flux.fromIterable(counts);
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
