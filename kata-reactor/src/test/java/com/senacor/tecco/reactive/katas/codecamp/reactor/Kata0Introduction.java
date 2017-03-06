package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.util.WaitMonitor;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;
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
        Flux<String> mamaFlux = Flux.just("Mama")
                // do some transformations
                .map(name -> name + " " + name);

        // subscribe as special TestObserver
        StepVerifier.create(mamaFlux)
                // assert 1 emitted element with the given value
                .expectNext("Mama Mama")
                // assert observable completed successfully
                .verifyComplete();
    }

    @Test
    public void testingWithWaitMonitor() throws Exception {
        print("create the WaitMonitor before subscribing to the Observable");
        WaitMonitor waitMonitor = new WaitMonitor();

        // create
        Flux.just("Mama")
                // do some transformations...
                .map(name -> name + " " + name)
                // run on another Thread, to demonstrate the WaitMonitor
                .subscribeOn(Schedulers.single())
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
