package com.senacor.tecco.reactive.util;

import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class FlakinessFunctionTest {

    @Test
    public void failWithProbability1percent() throws Exception {
        FlakinessFunction flakinessFunction = FlakinessFunction.failWithProbability(1);
        Observable.range(1, 100)
                .map(i -> {
                    try {
                        flakinessFunction.failOrPass("");
                        return Observable.just(1);
                    } catch (RuntimeException e) {
                        return Observable.empty();
                    }
                }).count()
                .doOnSuccess(value -> print(value))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(value -> value > 90)
        ;
    }

    @Test
    public void failWithProbability99percent() throws Exception {
        FlakinessFunction flakinessFunction = FlakinessFunction.failWithProbability(99);
        Observable.range(1, 100)
                .flatMap(i -> {
                    try {
                        flakinessFunction.failOrPass("");
                        return Observable.just(1);
                    } catch (RuntimeException e) {
                        return Observable.empty();
                    }
                }).count()
                .doOnSuccess(value -> print(value))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(value -> value < 5)
        ;
    }

    @Test
    public void failWithProbability50percent() throws Exception {
        FlakinessFunction flakinessFunction = FlakinessFunction.failWithProbability(50);
        Observable.range(1, 100)
                .flatMap(i -> {
                    try {
                        flakinessFunction.failOrPass("");
                        return Observable.just(1);
                    } catch (RuntimeException e) {
                        return Observable.empty();
                    }
                }).count()
                .doOnSuccess(value -> print(value))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(value -> value > 30 && value < 70)
        ;
    }

    @Test
    public void failWithProbability99percentWithMethodMatch() throws Exception {
        FlakinessFunction flakinessFunction = FlakinessFunction.failWithProbability(99, "test");
        Observable.range(1, 100)
                .flatMap(i -> {
                    try {
                        flakinessFunction.failOrPass("test");
                        return Observable.just(1);
                    } catch (RuntimeException e) {
                        return Observable.empty();
                    }
                }).count()
                .doOnSuccess(value -> print(value))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(value -> value < 5)
        ;
    }

    @Test
    public void failWithProbability99percentNoMethodMatch() throws Exception {
        FlakinessFunction flakinessFunction = FlakinessFunction.failWithProbability(99, "test");
        Observable.range(1, 100)
                .flatMap(i -> {
                    try {
                        flakinessFunction.failOrPass("other");
                        return Observable.just(1);
                    } catch (RuntimeException e) {
                        return Observable.empty();
                    }
                }).count()
                .doOnSuccess(value -> print(value))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(100L)
        ;
    }

    @Test
    public void failCountDown() throws Exception {
        FlakinessFunction function = FlakinessFunction.failCountDown(3);
        Observable.range(1, 10)
                .flatMap(i -> {
                    try {
                        function.failOrPass("test");
                        return Observable.just(1);
                    } catch (RuntimeException e) {
                        return Observable.empty();
                    }
                }).count()
                .doOnSuccess(value -> print(value))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(7L)
        ;
    }

    @Test
    public void failCountDownMethodMatch() throws Exception {
        FlakinessFunction function = FlakinessFunction.failCountDown(3, "test");
        Observable.range(1, 10)
                .flatMap(i -> {
                    try {
                        function.failOrPass("test");
                        return Observable.just(1);
                    } catch (RuntimeException e) {
                        return Observable.empty();
                    }
                }).count()
                .doOnSuccess(value -> print(value))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(7L)
        ;
    }

    @Test
    public void failCountDownNoMethodMatch() throws Exception {
        FlakinessFunction function = FlakinessFunction.failCountDown(3, "test");
        Observable.range(1, 10)
                .flatMap(i -> {
                    try {
                        function.failOrPass("other");
                        return Observable.just(1);
                    } catch (RuntimeException e) {
                        return Observable.empty();
                    }
                }).count()
                .doOnSuccess(value -> print(value))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(10L)
        ;
    }
}