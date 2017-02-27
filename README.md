rxjava-katas
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