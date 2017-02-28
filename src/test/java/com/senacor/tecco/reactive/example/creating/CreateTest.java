package com.senacor.tecco.reactive.example.creating;

import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 * @version 2.0
 */
public class CreateTest {

    @Test
    public void testCreate() throws Exception {

        Observable.create(subscriber -> {
            try {
                subscriber.onNext("first");
                subscriber.onNext("second");
                subscriber.onNext("3rd");
                subscriber.onNext("...");
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        })
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testCreateWithFunction() throws Exception {
        Observable<String> obs = Observable.create(subscriber -> {
            try {
                subscriber.onNext(getValue());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

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
