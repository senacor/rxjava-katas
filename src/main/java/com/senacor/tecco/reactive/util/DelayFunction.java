package com.senacor.tecco.reactive.util;

import java.util.Arrays;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Michael Omann
 * @author Andreas Keefer
 */
@FunctionalInterface
public interface DelayFunction {

    long delay(String methodName);

    static DelayFunction staticDelay(long millis, String... methodNames) {
        checkArgument(millis > 0);
        return (String methodName) -> {
            if (methodNames.length == 0 || Arrays.asList(methodNames).contains(methodName)) {
                return millis;
            }
            return 0;
        };
    }

    static DelayFunction withNoDelay() {
        return (String methodName) -> 0;
    }


    static DelayFunction withRandomDelay(int lowerBoundMillis, int upperBoundMillis, String... methodNames) {
        checkArgument(lowerBoundMillis > 0);
        checkArgument(upperBoundMillis > 0);
        checkArgument(upperBoundMillis > lowerBoundMillis);
        final Random rnd = new Random();
        return (String methodName) -> {
            if (methodNames.length == 0 || Arrays.asList(methodNames).contains(methodName)) {
                return rnd.nextInt(upperBoundMillis - lowerBoundMillis) + lowerBoundMillis;
            }
            return 0;
        };
    }
}
