package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Andreas Keefer
 */
public class Kata0Introduction {

    @Test
    public void testingWithRxJavasBuildInTestingSupport() throws Exception {
        // create
        Observable.just("Mama")
                // do some transformations
                .map(name -> name + " " + name)
                // subscribe as special TestObserver
                .test()
                // wait with a timeout until all Elements has been emitted by the Observable
                // this is only necessary when working with Schedulers
                .awaitDone(1, SECONDS)
                // assert 1 emitted element with the given value
                .assertValue("Mama Mama")
                // assert observable completed successfully
                .assertComplete();
    }

    @Test
    public void testingWithWaitMonitor() throws Exception {
        print("create the WaitMonitor before subscribing to the Observable");
        WaitMonitor waitMonitor = new WaitMonitor();

        // create
        Observable.just("Mama")
                // do some transformations...
                .map(name -> name + " " + name)
                // run on another Thread, to demonstrate the WaitMonitor
                .observeOn(Schedulers.newThread())
                // subscribe as a ordinary Observer
                .subscribe(next -> {
                            // print and assert the emitted value in the "onNext" Handler
                            print("next: %s", next);
                            assertThat(next, is("Mama Mama"));
                        },
                        error -> {
                            // handle Errors in the "onError" Handler
                            error.printStackTrace();
                            fail("No Error expected");
                        },
                        () -> {
                            // onComplete Handler
                            print("complete!");
                            // trigger waitMonitor.complete() when execution ended as expected
                            waitMonitor.complete();
                        });

        print("block the main thread with a timeout and wait till execution ended as expected.");
        waitMonitor.waitFor(1, SECONDS);
    }
}
