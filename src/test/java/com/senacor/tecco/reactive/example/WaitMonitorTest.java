package com.senacor.tecco.reactive.example;

import com.senacor.tecco.reactive.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class WaitMonitorTest {

    @Test
    public void testTimeout() throws Exception {
        WaitMonitor monitor = new WaitMonitor();
        monitor.waitFor(100, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isComplete());
    }

    @Test
    public void testWaitFor() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Observable.just("test")
                .subscribeOn(Schedulers.computation())
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete
                );
        monitor.waitFor(100, TimeUnit.MILLISECONDS);
        assertTrue(monitor.isComplete());
    }
}