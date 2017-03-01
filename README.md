RxJava
============

### getting started / DOKU

- http://reactivex.io/documentation/operators.html
- http://rxmarbles.com/

### RxJava 2 update
https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0

##### RxJava 1.x Timeline
- June 1, 2017 - feature freeze (no new operators), only bugfixes
- March 31, 2018 - end of life, no further development

##### important changes regarding upgrading from Rxjava 1.x
- Nulls (https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#nulls): 
RxJava 2.x no longer accepts `null` values
- Observable and Flowable (https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#observable-and-flowable): 
Observable is the non-backpressure Version and only Flowable supports backpressure.
- Subscription (https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#subscription):
`rx.Subscription` has been renamed to `io.reactivex.Disposable`
- Schedulers (https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#schedulers):
`test()` is no longer available in the `Schedulers` Class, use `new TestScheduler()`
- toBlocking -> blockingGet

##### new features
- Maybe (https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#maybe): 
Conceptually, it is a union of `Single` and `Completable` providing the means to capture an emission pattern where there could be 0 or 1 item or an error signalled by some reactive source.
- Backpressure (https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#backpressure)
- Reactive-Streams compliance (https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#reactive-streams-compliance):
- Runtime hooks (https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#runtime-hooks): 
Hooks can now be changed at runtime. In 1.x you get an Exception if you change an hook in `RxJavaPlugins`


Reactor
=======

### getting started / DOKU

- http://projectreactor.io/docs


Reactor (3.0.5.RELEASE) vs. RxJava 2 (2.0.6)
====================

- **Java**:
<br>- Reactor requires Java 8
<br>- RxJava requires Java 6
- **dependencies**:
<br>- Reactor has reactive-streams, jsr305 (optional) and slf4j-api (optional) as dependencies
<br>- RxJava has only reactive-streams as a dependency
- **footprint** (without dependencies):
<br>- Reactor: 1,1 MB
<br>- RxJava: 2.1 MB
- **Debugging**:
<br>- Reactor has a nice debugging feature (https://projectreactor.io/docs/core/release/reference/docs/index.html#_reading_a_stack_trace_in_debug_mode)
<br>- RxJava 2 has no direct support, but brings the basics to build debugging features (https://github.com/ReactiveX/RxJava/wiki/Plugins). For 1.x (maybe works also on 2.x, but not tested) exists an (outdated?) debugging module (https://github.com/ReactiveX/RxJavaDebug)
- **Complexity**:
<br>- Reactor is a bit smaller and has no "legacy" stuff like RxJava.
<br>- RxJava 2 is a bit more complex, regarding RxJava 1.x
- "**Datatypes**":
<br>- Reactor has just Mono(0-1) and Flux (0..n)
<br>- RxJava has Single (1), Maybe (0-1), Completable (0), Observable (0-n, no backpressure) and Flowable (0-n, backpressure)
- **Doku** (March 2017)
<br>- Reactor has a good, solid Doku with some TODO's/missing pats for some advanced Features 
<br>- RxJava 2's Doku is not jet updated in total from 1.x to 2.x, but 1.x Doku is great.
- **Testing**:
<br> both have more or less equal testing support
- **operators**:
<br> no relevant difference
- **performance** (https://github.com/akarnokd/akarnokd-misc/issues/2):
<br>- Reactor is generally 10-50% better in a Java 8 environment due to more inlined nature and even fewer allocations than RxJava 2
<br>- RxJava 2.x is generally better in terms of performance and memory utilization than 1.x; most lower performing components can be optimized further
- **...**:
<br>- Reactor
<br>- RxJava