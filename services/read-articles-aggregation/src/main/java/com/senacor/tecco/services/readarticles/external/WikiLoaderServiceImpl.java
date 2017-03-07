package com.senacor.tecco.services.readarticles.external;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

import static com.senacor.tecco.services.readarticles.WrongStatusException.okFilter;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class WikiLoaderServiceImpl implements WikiLoaderService{

    public static final String ARTICLE_ENDPOINT = "/article/";

    private final WebClient wikiLoaderClient;

    public WikiLoaderServiceImpl(WebClient wikiLoaderClient) {
        this.wikiLoaderClient = wikiLoaderClient;
    }

    @Override
    public Mono<Article> fetchArticle(String articleName) {
        return wikiLoaderClient.get()
                               .uri(ARTICLE_ENDPOINT + URLEncoder.encode(articleName))
                               .exchange()
                               .doOnNext(okFilter())
                               .flatMap(r -> r.bodyToFlux(Article.class))
                               .single();
    }
}
