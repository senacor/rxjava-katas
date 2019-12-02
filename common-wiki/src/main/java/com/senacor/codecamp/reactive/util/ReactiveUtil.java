package com.senacor.codecamp.reactive.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return Observable.<Integer>create(s -> {
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
        }).subscribeOn(Schedulers.newThread());
    }

    public static <T> T print(T toPrint, Object... args) {
        System.out.println(getThreadId() + (ArrayUtils.isEmpty(args)
                ? toPrint.toString()
                : String.format(toPrint.toString(), args)
        ));
        return toPrint;
    }

    public static String findValue(String text, String key) {
        Pattern pattern = Pattern.compile(key + " ?= ?([\\d,]*)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static Scheduler newRxScheduler(int size, String name) {
        return Schedulers.from(newExecutor(size, name));
    }

    public static reactor.core.scheduler.Scheduler newReactorScheduler(int size, String name) {

        return reactor.core.scheduler.Schedulers.fromExecutor(newExecutor(size, name));
    }

    private static ExecutorService newExecutor(int size, String name) {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(name + "-%d")
                .setDaemon(true)
                .build();
        return Executors.newFixedThreadPool(size, threadFactory);
    }

    public static Scheduler newCachedThreadPoolScheduler(String name) {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(name + "-%d")
                .setDaemon(true)
                .build();
        return Schedulers.from(Executors.newCachedThreadPool(threadFactory));
    }

    /**
     * @see StringUtils#abbreviate(String, int)
     */
    public static String abbreviateWithoutNewline(String s, int maxWidth) {
        return null == s ? null :
                StringUtils.abbreviate(s, maxWidth).replaceAll("\\r\\n|\\r|\\n", " ");
    }
}
