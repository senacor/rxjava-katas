package com.senacor.codecamp.reactive.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Michael Omann
 */
@FunctionalInterface
public interface FlakinessFunction {

    void failOrPass(String methodName) throws RuntimeException;

    static FlakinessFunction failWithProbability(int failPercentage, String... methodNames) {
        checkArgument(failPercentage < 100 && failPercentage > 0);

        final Random rnd = new Random();
        return (String methodName) -> {
            if (methodNames.length == 0 || Arrays.asList(methodNames).contains(methodName)) {
                int i = rnd.nextInt(100);
                if (!(i - failPercentage > 0)) {
                    throw new UncheckedIOException(new IOException("Random IO-Error"));
                }
            }
        };
    }


    static FlakinessFunction noFlakiness() {
        return (String methodName) -> {
        };
    }

    static FlakinessFunction alwaysFail(String... methodNames) {
        return (String methodName) -> {
            if (methodNames.length == 0 || Arrays.asList(methodNames).contains(methodName)) {
                throw new UncheckedIOException(new IOException("Random IO-Error"));
            }
        };
    }

    /**
     * Will fail until the invocation count reaches initialCount.
     * When initialCount = 3 the first 3 executions will fail
     */
    static FlakinessFunction failCountDown(int initialCount, String... methodNames) {
        checkArgument(initialCount >= 1);
        AtomicInteger count = new AtomicInteger(initialCount);
        return (String methodName) -> {
            if (methodNames.length == 0 || Arrays.asList(methodNames).contains(methodName)) {
                int remainingFails = count.decrementAndGet();
                if (remainingFails >= 0) {
                    throw new UncheckedIOException(new IOException("Random IO-Error"));
                }
            }
        };
    }

}
