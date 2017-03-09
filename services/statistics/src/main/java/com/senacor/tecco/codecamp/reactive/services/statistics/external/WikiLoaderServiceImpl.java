package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import com.senacor.tecco.codecamp.reactive.services.statistics.WrongStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

/**
 * @author Daniel Heinrich
 */
@Service
public class WikiLoaderServiceImpl implements WikiLoaderService {

    public static final String ARTICLE_ENDPOINT = "/article/";

    private final WebClient articleClient;

    @Autowired
    public WikiLoaderServiceImpl(WebClient articleClient) {
        this.articleClient = articleClient;
    }

    @Override
    public Mono<Article> fetchArticle(String articleName) {
        return articleClient.get()
                               .uri(ARTICLE_ENDPOINT + URLEncoder.encode(articleName))
                               .exchange()
                               .doOnNext(WrongStatusException.okFilter())
                               .flatMap(r -> r.bodyToFlux(Article.class))
                               .single();
    }
}
