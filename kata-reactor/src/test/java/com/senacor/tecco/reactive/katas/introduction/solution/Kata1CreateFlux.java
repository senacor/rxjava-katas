package com.senacor.tecco.reactive.katas.introduction.solution;

import org.junit.Test;
import reactor.core.publisher.Flux;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class Kata1CreateFlux {

    @Test
    public void createAnFlux() throws Exception {
        final String planeType = "Boeing 737";

        // 1) create an observable that emits the plane type
        Flux<String> obs = Flux.just(planeType);

        // 2) subscribe to the observable and print the plane type
        obs.subscribe(next -> print("next: %s", next),
                Throwable::printStackTrace,
                () -> print("complete!"));
    }
}
