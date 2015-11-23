package com.senacor.tecco.reactive.example.transforming;

import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class GroupByTest {
    @Test
    public void testGroupBy() throws Exception {
        Subscription subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .groupBy(index -> index % 2 == 0 ? "gerade" : "ungerade")
                .subscribe(next -> {
                            print("next: %s", next);
                            next.subscribe(next2 -> print("key=" + next.getKey() + " value=" + next2));
                        },
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.unsubscribe();

    }
}
