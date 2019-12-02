package com.senacor.codecamp.reactive.example.creating;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class JustTest {

    @Test
    public void testJust() throws Exception {
        Observable.just("first")
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));

        Observable.just("first", "second", "3rd", "...")
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));
    }

    @Test
    public void testJustWithFunctionCall() throws Exception {
        Observable<String> obs = Observable.just(getValue());

        ReactiveUtil.print("Observable created");

        obs.subscribe(next -> ReactiveUtil.print("next: %s", next),
                Throwable::printStackTrace,
                () -> ReactiveUtil.print("complete!"));
    }

    public String getValue() {
        ReactiveUtil.print("getValue invoked");
        return "first";
    }
}