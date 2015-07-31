package com.senacor.tecco.codecamp.reactive.scheduling;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class SchdulingTest {
    @Test
    public void testSubscribeOn() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Subscription subscription = Observable.range(1, 10)
                .doOnNext(integer -> print("before getData: %s", integer))
                .flatMap(integet -> SchdulingTest.getDataSync(integet)
                        .subscribeOn(Schedulers.io()))
                .doOnNext(integer -> print("after getData: %s", integer))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(5000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testObserveOn() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Subscription subscription = Observable.range(1, 5)
                .doOnNext(integer -> print("before getData: %s", integer))
                .observeOn(Schedulers.io())
                .flatMap(SchdulingTest::getDataSync)
                .doOnNext(integer -> print("after getData: %s", integer))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(5000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    static Observable<Integer> getDataAsync(int i) {
        return getDataSync(i)
                .subscribeOn(Schedulers.io());
    }

    static Observable<Integer> getDataSync(final int i) {
        print("getDataSync(%s)", i);
        return Observable.create((Subscriber<? super Integer> s) -> {
            // simulate latency (blocking)
            try {
                print("sleeping 500 ms ...");
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            s.onNext(i);
            s.onCompleted();
        });
    }
}
