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
public class WindowTest {

    @Test
    public void testWindow() throws Exception {
        Subscription subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .window(5)
                .subscribe(next -> {
                            System.out.println(getThreadId() + "next: " + next);
                            next.subscribe(window -> print("window: " + window));
                        },
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.unsubscribe();
    }

    @Test
    public void testWindowWithTimespan() throws Exception {
        Subscription subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .window(350, TimeUnit.MILLISECONDS, 5)
                .subscribe(next -> {
                            System.out.println(getThreadId() + "next: " + next);
                            next.subscribe(window -> print("window: " + window));
                        },
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.unsubscribe();
    }
}
