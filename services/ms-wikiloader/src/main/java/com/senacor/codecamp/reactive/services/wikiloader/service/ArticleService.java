package com.senacor.codecamp.reactive.services.wikiloader.service;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Andreas Keefer
 */
@Service
public class ArticleService {

    private final WikiService wikiService;
    private final CountService countService;
    private final RatingService ratingService;
    private final PublisherCache<String, String> cache;

    @Autowired
    public ArticleService(WikiService wikiService, CountService countService, RatingService ratingService,
                          @Value("${article.cache.size}") int cacheSize) {
        this.wikiService = wikiService;
        this.countService = countService;
        this.ratingService = ratingService;
        this.cache = new PublisherCache<>(this::getArticle, cacheSize);
    }

    /**
     * @param articleName article name
     * @return fetched article from wikipedia as a media wiki formatted string
     */
    public Mono<String> fetchArticle(String articleName) {
        return cache.lookup(articleName);
    }

    /**
     * @param articleName article name
     * @return fetched article from wikipedia as a media wiki formatted string
     */
    public String fetchArticleNonReactive(String articleName) {
        return fetchArticle(articleName).block();
    }

    /**
     * @param articleName article name
     * @return a rating of the wiki article from 1 to 5 'stars'
     */
    public Mono<Integer> rate(String articleName) {
        return getParsedArticle(articleName)
                .map(ratingService::rate);
    }

    /**
     * @param articleName article name
     * @return count of all words in this article
     */
    public Mono<Integer> countWords(String articleName) {
        return getParsedArticle(articleName)
                .map(countService::countWords);
    }

    private Mono<String> getArticle(String name) {
        return wikiService.fetchArticleNonBlocking(name);
    }

    private Mono<ParsedPage> getParsedArticle(String name) {
        return fetchArticle(name)
                .map(wikiService::parseMediaWikiText);
    }
}
