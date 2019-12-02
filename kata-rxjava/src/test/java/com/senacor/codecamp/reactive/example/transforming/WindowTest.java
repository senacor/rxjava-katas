package com.senacor.codecamp.reactive.example.transforming;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.getThreadId;
import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class WindowTest {

    @Test
    public void testWindow() throws Exception {
        Disposable subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .window(5)
                .subscribe(next -> {
                            System.out.println(getThreadId() + "next: " + next);
                            next.subscribe(window -> print("window: " + window));
                        },
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.dispose();
    }

    @Test
    public void testWindowWithTimespan() throws Exception {
        Disposable subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .window(350, TimeUnit.MILLISECONDS, 5)
                .subscribe(next -> {
                            System.out.println(getThreadId() + "next: " + next);
                            next.subscribe(window -> print("window: " + window));
                        },
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.dispose();
    }
}
