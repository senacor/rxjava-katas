package com.senacor.tecco.codecamp.reactive;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.ArrayUtils;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
        System.out.println(getThreadId() + (ArrayUtils.isEmpty(args)
                ? toPrint.toString()
                : String.format(toPrint.toString(), args)
        ));
        return toPrint;
    }

    public static Scheduler newScheduler(int size, String name) {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(name + "-%d")
                .setDaemon(true)
                .build();
        return Schedulers.from(Executors.newFixedThreadPool(size, threadFactory));
    }

    public static Scheduler newCachedThreadPoolScheduler(String name) {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(name + "-%d")
                .setDaemon(true)
                .build();
        return Schedulers.from(Executors.newCachedThreadPool(threadFactory));
    }
}
