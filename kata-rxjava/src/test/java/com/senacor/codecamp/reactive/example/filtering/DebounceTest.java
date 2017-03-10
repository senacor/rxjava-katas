package com.senacor.codecamp.reactive.example.filtering;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class DebounceTest {

    @Test
    public void testDebounce() throws Exception {
        Disposable subscription = intermittentBursts()
                .debounce(10, TimeUnit.MILLISECONDS)
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));

        Thread.sleep(3000);
        subscription.dispose();
    }

    /**
     * This is an artificial source to demonstrate an infinite stream that bursts intermittently
     */
    private static Observable<Integer> intermittentBursts() {
        return Observable.create((ObservableEmitter<Integer> s) -> {
            while (!s.isDisposed()) {
                // burst some number of items
                for (int i = 0; i < Math.random() * 20; i++) {
                    s.onNext(i);
                }
                try {
                    // sleep for a random amount of time
                    // NOTE: Only using Thread.sleep here as an artificial demo.
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (Exception e) {
                    // do nothing
                }
            }
        }).subscribeOn(Schedulers.newThread()); // use newThread since we are using sleep to block
    }
}
