package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SchedulerExperiments {
    private static class Timer {
        private long start;
        private long end;

        public void start() {
            start = System.currentTimeMillis();
        }

        public synchronized void end() {
            end = System.currentTimeMillis();
            System.out.println(end - start);
        }
    }

    public static void main(String[] args) throws Exception {
        Timer timer = new Timer();
        timer.start();

        Observable<Integer> range = Observable.from(IntStream.range(1, 50).boxed().collect(Collectors.toList()));

        range
               // .subscribeOn(Schedulers.newThread())
                .flatMap(i-> Observable.defer(() -> Observable.just(SchedulerExperiments.slowService(i)))
                        .subscribeOn(Schedulers.newThread())
                )
                .subscribe(System.out::println, Throwable::printStackTrace, timer::end);

        Thread.sleep(1000*30);
    }

    private static Integer slowService(Integer integer) {
        try {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName());
            return integer;
        } catch (InterruptedException e) {
            return null;
        }
    }

}
