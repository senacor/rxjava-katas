package com.senacor.codecamp.reactive.example.filtering;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.Observable;
import org.junit.Test;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class FirstTest {
    @Test
    public void testFirst() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .firstElement()
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));
    }

    @Test
    public void testFirstOrDefault() throws Exception {
        Observable.empty()
                .first(99)
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace);
    }
}
