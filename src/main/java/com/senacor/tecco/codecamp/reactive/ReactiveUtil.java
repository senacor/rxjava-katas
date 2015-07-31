package com.senacor.tecco.codecamp.reactive;

import org.apache.commons.lang.math.RandomUtils;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author Andreas Keefer
 */
public class ReactiveUtil {

    public static String getThreadId() {
        return Thread.currentThread().getName() + ": ";
    }

    public static void randomDelay(int delayMaxExclusive) {
        fixedDelay(RandomUtils.nextInt(delayMaxExclusive));
    }

    public static void fixedDelay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Observable<Integer> burstSource() {
        return Observable.create((
                Subscriber<? super Integer> s) -> {
            while (!s.isUnsubscribed()) {
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
        }).subscribeOn(Schedulers.newThread());
    }

    public static <T> T print(T toPrint, Object... args) {
        System.out.println(getThreadId() + String.format(toPrint.toString(), args));
        return toPrint;
    }
}
