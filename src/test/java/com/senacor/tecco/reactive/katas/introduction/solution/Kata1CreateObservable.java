package com.senacor.tecco.reactive.katas.introduction.solution;

import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String planeType = "Boeing 737";

        // 1) create an observable that emits the plane type
        Observable<String> obs = Observable.just(planeType, "767");

        // 2) subscribe to the observable and print the plane type
        obs.subscribe(next -> print("next: %s", next),
                Throwable::printStackTrace,
                () -> print("complete!"));

    }


}
