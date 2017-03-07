package com.senacor.tecco.services.readarticles.external;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public interface MetricsService {

    Flux<Integer> fetchWordcount(String articleText);

    Flux<Integer> fetchWordcount(Publisher<String> articleText);

    Flux<Integer> fetchRating(String articleText);

    Flux<Integer> fetchRating(Publisher<String> articleText);
}
