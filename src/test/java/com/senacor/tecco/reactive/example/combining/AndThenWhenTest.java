package com.senacor.tecco.reactive.example.combining;

import org.junit.Test;
import rx.Observable;
import rx.observables.JoinObservable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class AndThenWhenTest {
    @Test
    public void testAndThenWhen() throws Exception {
        Observable<Integer> one = Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Observable<Integer> two = Observable.just(10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

        JoinObservable.when(
                JoinObservable.from(one)
                        .and(two)
                        .then((i1, i2) -> i1 + i2)
        )
                .toObservable()
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }
}
