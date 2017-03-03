package com.senacor.tecco.reactive.example.creating;

import com.senacor.tecco.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.util.ReactiveUtil.getThreadId;
import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class TimerTest {
    @Test
    public void testTimer() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        System.out.println(getThreadId() + "start...");
        Observable.timer(2, TimeUnit.SECONDS)
                .map(ignore -> "delayed ...")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete
                );
        print("observable subscribed...");
        monitor.waitFor(3, TimeUnit.SECONDS);
    }
}