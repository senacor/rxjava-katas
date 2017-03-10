package com.senacor.codecamp.reactive.example.transforming;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class GroupByTest {
    @Test
    public void testGroupBy() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .groupBy(index -> index % 2 == 0 ? "gerade" : "ungerade")
                .doOnNext(next -> {
                    ReactiveUtil.print("next: %s", next);
                    next.subscribe(next2 -> ReactiveUtil.print("key=" + next.getKey() + " value=" + next2));
                })
                .doOnComplete(() -> ReactiveUtil.print("complete!"))
                .doOnError(Throwable::printStackTrace)
                .test()
                .awaitTerminalEvent(2, TimeUnit.SECONDS);
    }
}
