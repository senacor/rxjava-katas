package com.senacor.tecco.reactive.util;

import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Michael Omann
 */
@FunctionalInterface
public interface DelayFunction {

    long delay(String methodName);

    static DelayFunction staticDelay(long millis) {
        checkArgument(millis > 0);
        return (String methodName) -> millis;
    }

    static DelayFunction withNoDelay() {
        return (String methodName) -> 0;
    }


    static DelayFunction withRandomDelay(int lowerBoundMillis, int upperBoundMillis) {
        checkArgument(lowerBoundMillis > 0);
        checkArgument(upperBoundMillis > 0);
        checkArgument(upperBoundMillis > lowerBoundMillis);
        final Random rnd = new Random();
        return (String methodName) -> rnd.nextInt(upperBoundMillis - lowerBoundMillis) + lowerBoundMillis;
    }
}
