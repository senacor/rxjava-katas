package com.senacor.tecco.reactive.example.creating;

import com.senacor.tecco.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class TimerTest {
    @Test
    public void testTimer() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        System.out.println(getThreadId() + "start...");
        Observable.timer(2, TimeUnit.SECONDS)
                .map(ignore -> "deleyed ...")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete
                );
        print("observable subscribeed...");
        monitor.waitFor(3, TimeUnit.SECONDS);
    }
}