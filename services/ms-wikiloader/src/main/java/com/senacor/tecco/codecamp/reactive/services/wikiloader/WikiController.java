package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
@RestController
public class WikiController {

    private final WikiService wikiService;

    @Autowired
    public WikiController(WikiService wikiService) {
        this.wikiService = wikiService;
    }

    @GetMapping("/article/{name}")
    public Mono<Article> fetchArticle(@PathVariable String name) {
        return Mono.from(wikiService.fetchArticleFlux(name)
                .subscribeOn(Schedulers.elastic()))
                .map(content -> new Article(name, content));
    }

    @GetMapping(value = "/article/stream")
    public Flux<Article> fetchArticles(@RequestBody Flux<String> names) {
        return names
                .doOnNext(next -> print("before fetchArticleFlux: " + next))
                .flatMap(articleName -> wikiService.fetchArticleFlux(articleName)
                        .subscribeOn(Schedulers.elastic())
                        .map(content -> new Article(articleName, content))
                ).doOnNext(next -> print("after fetchArticleFlux: " + next));
    }

    @GetMapping("/article/test")
    public Flux<Article> fetchArticlesStreamTest(@RequestBody List<String> names) {
        return Flux.fromIterable(names)
                .doOnNext(next -> print("before fetchArticlesStreamTest: " + next))
                .flatMap(articleName -> wikiService.fetchArticleFlux(articleName)
                        .subscribeOn(Schedulers.elastic())
                        .map(content -> new Article(articleName, content))
                ).doOnNext(next -> print("after fetchArticlesStreamTest: " + next));
    }
}
