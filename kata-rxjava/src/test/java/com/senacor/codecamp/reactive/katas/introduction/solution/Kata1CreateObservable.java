package com.senacor.codecamp.reactive.katas.introduction.solution;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String planeType = "Boeing 737";

        // 1) create an observable that emits the plane type
        Observable<String> obs = Observable.just(planeType);

        // 2) subscribe to the observable and print the plane type
        obs.subscribe(next -> print("next: %s", next),
                Throwable::printStackTrace,
                () -> print("complete!"));
    }
}
