package com.senacor.codecamp.reactive.katas.introduction;

import io.reactivex.Observable;
import org.junit.Test;

/**
 * @author Dr. Michael Menzel
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String planeType = "Boeing 737";

        // 1) create an observable that emits the plane type
        Observable.just(planeType)
        // 2) subscribe to the observable and print the plane type
                    .subscribe(System.out::println);

    }


}
