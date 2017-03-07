package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import com.senacor.tecco.codecamp.reactive.services.statistics.WrongStatusException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class WikiLoaderServiceImpl implements WikiLoaderService {

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
                               .doOnNext(WrongStatusException.okFilter())
                               .flatMap(r -> r.bodyToFlux(Article.class))
                               .single();
    }
}
