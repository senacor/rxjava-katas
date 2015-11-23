package com.senacor.tecco.reactive.example.filtering;

import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class ElementAtTest {
    @Test
    public void testElementAt() throws Exception {
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(3)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testElementAtOrDefault() throws Exception {
        Observable.just(1, 2, 3, 4, 5)
                .elementAtOrDefault(99, 100)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}
