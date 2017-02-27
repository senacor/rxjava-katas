package com.senacor.tecco.reactive.example.combining;

import hu.akarnokd.rxjava2.joins.JoinObservable;
import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas
 * @version 2.0
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
                .forEach(next -> print("next: %s", next));
    }
}
