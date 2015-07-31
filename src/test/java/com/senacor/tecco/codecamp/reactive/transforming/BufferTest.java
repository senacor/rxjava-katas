package com.senacor.tecco.codecamp.reactive.transforming;

import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class BufferTest {
    @Test
    public void testBuffer() throws Exception {
        Subscription subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .buffer(5)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.unsubscribe();
    }

    @Test
    public void testBufferWithTimespan() throws Exception {
        Subscription subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .buffer(350, TimeUnit.MILLISECONDS, 5)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.unsubscribe();
    }
}
