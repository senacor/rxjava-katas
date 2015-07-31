package com.senacor.tecco.codecamp.reactive.error;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class CatchTest {
    @Test
    public void testOnErrorResumeNext() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.timer(100, TimeUnit.MILLISECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.timer(500, TimeUnit.MILLISECONDS)
                .map(value -> {
                    throw new NullPointerException("dooo");
                });
        Observable<String> stream3 = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .map(value -> "3rd " + value);

        Subscription subscription = Observable.merge(stream1, stream2, stream3)
                .onErrorResumeNext(Observable.just("onErrorResumeNext"))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        monitor.waitFor(1500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testOnExceptionResumeNext() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.timer(100, TimeUnit.MILLISECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.timer(500, TimeUnit.MILLISECONDS)
                .map(value -> {
                    throw new NullPointerException("dooo");
                });
        Observable<String> stream3 = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .map(value -> "3rd " + value);

        Subscription subscription = Observable.merge(stream1, stream2, stream3)
                .onExceptionResumeNext(Observable.just("onExceptionResumeNext"))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        monitor.waitFor(1500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testOnErrorReturn() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.timer(100, TimeUnit.MILLISECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.timer(500, TimeUnit.MILLISECONDS)
                .map(value -> {
                    throw new NullPointerException("dooo");
                });
        Observable<String> stream3 = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .map(value -> "3rd " + value);

        Subscription subscription = Observable.merge(stream1, stream2, stream3)
                .onErrorReturn(Throwable::getMessage)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(1500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }
}
