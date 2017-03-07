package com.senacor.tecco.services.aggregation;

import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

import static com.senacor.tecco.services.aggregation.WrongStatusException.okFilter;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class WikiLoaderService {

    public static final String ARTICLE_ENDPOINT = "/article/";

    private final WebClient wikiLoaderClient;

    public WikiLoaderService(WebClient wikiLoaderClient) {
        this.wikiLoaderClient = wikiLoaderClient;
    }

    public Mono<Article> fetchArticle(String articleName) {
        return wikiLoaderClient.get()
                               .uri(ARTICLE_ENDPOINT + URLEncoder.encode(articleName))
                               .exchange()
                               .doOnNext(okFilter())
                               .flatMap(r -> r.bodyToMono(Article.class))
                               .single();
    }
}
