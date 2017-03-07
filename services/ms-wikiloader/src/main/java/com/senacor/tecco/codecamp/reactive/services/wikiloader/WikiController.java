package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Andreas Keefer
 */
@RestController
@RequestMapping("/article")
public class WikiController {

    private final WikiService wikiService;
    private final CountService countService;
    private final RatingService ratingService;

    @Autowired
    public WikiController(WikiService wikiService, CountService countService, RatingService ratingService) {
        this.wikiService = wikiService;
        this.countService = countService;
        this.ratingService = ratingService;
    }

    @GetMapping("/{name}")
    public Mono<Article> fetchArticle(@PathVariable String name) {
        return wikiService.fetchArticleNonBlocking(name)
                          .map(content -> new Article(name, content));
    }

    private Mono<ParsedPage> getParsedArticle(String name) {
        return fetchArticle(name)
                .map(Article::getContent)
                .map(wikiService::parseMediaWikiText);
    }

    @GetMapping("/{name}/wordcount")
    public Mono<Integer> getWordCount(@PathVariable String name) {
        return getParsedArticle(name).map(countService::countWords);
    }

    @GetMapping("/{name}/rating")
    public Mono<Integer> getRating(@PathVariable String name) {
        return getParsedArticle(name).map(ratingService::rate);
    }
}
