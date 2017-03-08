package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.fasterxml.jackson.annotation.JsonView;
import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article.NameOnly;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

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
    private final Map<String, Article> cache;

    @Autowired
    public WikiController(WikiService wikiService, CountService countService, RatingService ratingService, Map<String, Article> cache) {
        this.wikiService = wikiService;
        this.countService = countService;
        this.ratingService = ratingService;
        this.cache = cache;
    }

    @GetMapping("/{name}")
    public Mono<Article> fetchArticle(@PathVariable String name) {
        return readCachedArticle(name)
                   .doOnNext(readArticles::onNext)
                   .otherwiseIfEmpty(wikiService.fetchArticleNonBlocking(name)
                                             .map(content -> new Article(name, content))
                                             .doOnNext(article -> cache.put(article.getName(), article))
                   );
    }

    @GetMapping("/readevents")
    @JsonView(NameOnly.class)
    public Publisher<Article> getReadStream(){
        return readArticles;
    }

    /**
     * @return Article from Cache or EMPTY
     */
    private Mono<Article> readCachedArticle(String name) {
        return Mono.justOrEmpty(cache.get(name))
                   .doOnNext(a -> print("cache hit for key '%s'", a.getName()));
    }

    private Mono<ParsedPage> getParsedArticle(String name) {
        return fetchArticle(name)
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
