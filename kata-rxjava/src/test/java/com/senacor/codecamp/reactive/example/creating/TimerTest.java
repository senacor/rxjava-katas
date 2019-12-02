package com.senacor.codecamp.reactive.example.creating;

import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.getThreadId;
import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

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