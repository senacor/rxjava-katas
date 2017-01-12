package com.senacor.tecco.reactive.example.transforming;

import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class GroupByTest {
    @Test
    public void testGroupBy() throws Exception {
        TestSubscriber testSubscriber = TestSubscriber.create();

        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .groupBy(index -> index % 2 == 0 ? "gerade" : "ungerade")
                .doOnNext(next -> {
                    print("next: %s", next);
                    next.subscribe(next2 -> print("key=" + next.getKey() + " value=" + next2));
                })
                .doOnCompleted(() -> print("complete!"))
                .doOnError(Throwable::printStackTrace)
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(2, TimeUnit.SECONDS);
    }
}
