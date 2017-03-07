package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public interface MetricsService {

    Flux<Integer> fetchWordcount(String articleName);

    Flux<Integer> fetchRating(String articleName);
}
