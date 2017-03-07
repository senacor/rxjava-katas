package com.senacor.tecco.services.readarticles.external;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

import static com.senacor.tecco.services.readarticles.WrongStatusException.okFilter;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public interface WikiLoaderService {

    Mono<Article> fetchArticle(String articleName);
}
