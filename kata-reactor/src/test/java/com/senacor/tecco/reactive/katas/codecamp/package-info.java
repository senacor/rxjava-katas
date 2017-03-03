/**
 * This Package contains exercises, so called "Kata's" for RxJava 2 and Reactor.
 * <p>
 * In some Katas a {@link com.senacor.tecco.reactive.WaitMonitor} is used.
 * This test helper is needed, when you don't use the UnitTesting features from RxJava/Reactor
 * and your Pipeline is not executed on the main thread. In this case you have to wait in the
 * main thread for the completion. this is the pattern:
 * <p>
 * WaitMonitor waitMonitor = new WaitMonitor();
 * <p>
 * Flux task ...
 * __.subscribe(next -> ...some OnNext handler,
 * _____________error -> ...some error handler,
 * _____________() -> waitMonitor.complete())
 * monitor.waitFor(5, TimeUnit.SECONDS);
 * <p>
 * The WaitMonitor will block the main thread until waitMonitor.complete() is called (pipeline execution has finished in the expected state)
 * OR the 5 seconds timeout has expired.
 *
 * @author Andreas Keefer
 */
package com.senacor.tecco.reactive.katas.codecamp;