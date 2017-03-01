package com.senacor.tecco.reactive.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Michael Omann
 */
@FunctionalInterface
public interface FlakinessFunction {

    void failOrPass(String methodName) throws RuntimeException;

    static FlakinessFunction failWithProbability(int failPercentage) {
        checkArgument(failPercentage < 100 && failPercentage > 0);

        final Random rnd = new Random();
        return (String methodName) -> {
            int i = rnd.nextInt(100);
            if (i - failPercentage > 0) throw new UncheckedIOException(new IOException("Random IO-Error"));
        };
    }


    static FlakinessFunction noFlakiness() {
        return (String methodName) -> {
        };
    }

    static FlakinessFunction alwaysFail() {
        return (String methodName) -> {
            throw new UncheckedIOException(new IOException("Random IO-Error"));
        };
    }

    /**
     * Will fail when the invocation count reaches initialCount.
     * When initialCount = 3 the execution fails at the 3rd invocation
     */
    static FlakinessFunction failCountDown(int initialCount) {
        checkArgument(initialCount >= 1);
        AtomicInteger count = new AtomicInteger(initialCount);
        return (String methodName) -> {
            int remainingFails = count.decrementAndGet();
            if (remainingFails <= 0) {
                throw new UncheckedIOException(new IOException("Random IO-Error"));
            }
        };
    }

}
