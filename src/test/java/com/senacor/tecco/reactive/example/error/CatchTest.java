package com.senacor.tecco.reactive.example.error;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.WaitMonitor;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 * @version 2.0
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

        Disposable subscription = Observable.merge(stream1, stream2, stream3)
                .onErrorResumeNext(Observable.just("onErrorResumeNext"))
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));

        monitor.waitFor(1500, TimeUnit.MILLISECONDS);
        subscription.dispose();
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

        Disposable subscription = Observable.merge(stream1, stream2, stream3)
                .onExceptionResumeNext(Observable.just("onExceptionResumeNext"))
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));

        monitor.waitFor(1500, TimeUnit.MILLISECONDS);
        subscription.dispose();
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

        Disposable subscription = Observable.merge(stream1, stream2, stream3)
                .onErrorReturn(Throwable::getMessage)
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(1500, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }
}
