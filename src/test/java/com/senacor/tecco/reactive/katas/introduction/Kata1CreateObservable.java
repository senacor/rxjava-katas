package com.senacor.tecco.reactive.katas.introduction;

import org.junit.Test;
import rx.Observable;

/**
 * @author Dr. Michael Menzel
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String planeType = "Boeing 737";

        // 1) create an observable that emits the plane type
        // 2) subscribe to the observable and print the plane type
        Observable.just(planeType).subscribe(System.out::println);
    }
}
