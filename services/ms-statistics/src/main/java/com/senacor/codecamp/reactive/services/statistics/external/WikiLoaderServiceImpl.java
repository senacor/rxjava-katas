package com.senacor.codecamp.reactive.services.statistics.external;

import com.senacor.codecamp.reactive.services.statistics.WrongStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.senacor.codecamp.reactive.services.statistics.external.URLEncoderUtil.urlEncode;

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
                               .uri(ARTICLE_ENDPOINT + urlEncode(articleName))
                               .exchange()
                               .doOnNext(WrongStatusException.okFilter())
                               .flatMapMany(r -> r.bodyToFlux(Article.class))
                               .single();
    }
}
