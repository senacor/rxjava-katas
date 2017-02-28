package com.senacor.tecco.reactive.example.creating;

import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class JustTest {

    @Test
    public void testJust() throws Exception {
        Observable.just("first")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Observable.just("first", "second", "3rd", "...")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testJustWithFunctionCall() throws Exception {
        Observable<String> obs = Observable.just(getValue());

        print("Observable created");

        obs.subscribe(next -> print("next: %s", next),
                Throwable::printStackTrace,
                () -> print("complete!"));
    }

    public String getValue() {
        print("getValue invoked");
        return "first";
    }
}