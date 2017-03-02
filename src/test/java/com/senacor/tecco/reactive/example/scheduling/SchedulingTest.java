package com.senacor.tecco.reactive.example.scheduling;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.senacor.tecco.reactive.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 * @see <a href="http://reactivex.io/documentation/scheduler.html">reactive.io</a>
 */
public class SchedulingTest {

    @Test
    public void testVisualizeSubscribeOnAndObserveOn() throws Exception {
        Thread.currentThread().setName("Main");

        Scheduler thread1 = Schedulers.from(Executors.newFixedThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat("Thread1").build()));
        Scheduler thread2 = Schedulers.from(Executors.newFixedThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat("Thread2").build()));
        Scheduler thread3 = Schedulers.from(Executors.newFixedThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat("Thread3").build()));

        final WaitMonitor monitor = new WaitMonitor();
        Disposable subscription = Observable.<Integer>create(subscriber -> {
            print("create");
            subscriber.onNext(1);
            subscriber.onComplete();
        }).subscribeOn(thread1)
                .map(next -> {
                    print("map: %s", next);
                    return next;
                })
                .observeOn(thread2)
                .flatMap(next -> {
                    print("flatMap: %s", next);
                    return Observable.just(next);
                })
                .observeOn(thread3)
                .subscribe(next -> print("Observer: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(500, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }

    @Test
    public void testSubscribeOn() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Disposable subscription = Observable.range(1, 10)
                .doOnNext(integer -> print("before getData: %s", integer))
                .flatMap(integer -> SchedulingTest.getDataSync(integer)
                        .subscribeOn(Schedulers.io()))
                .doOnNext(integer -> print("after getData: %s", integer))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(5000, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }

    @Test
    public void testObserveOn() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Disposable subscription = Observable.range(1, 5)
                .doOnNext(integer -> print("before getData: %s", integer))
                .observeOn(Schedulers.io())
                .flatMap(SchedulingTest::getDataSync)
                .doOnNext(integer -> print("after getData: %s", integer))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(5000, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }

    static Observable<Integer> getDataSync(final int i) {
        print("getDataSync(%s)", i);
        return Observable.create(s -> {
            // simulate latency (blocking)
            try {
                print("sleeping 500 ms ...");
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            s.onNext(i);
            s.onComplete();
        });
    }
}
