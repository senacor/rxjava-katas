package com.senacor.tecco.reactive.services.integration;

import com.google.common.collect.Iterables;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;

/**
 * @author Andreas Keefer
 */
public class BlackHoleDatabaseImpl implements BlackHoleDatabase {

    @Override
    public long saveOne(Object object) {
        int delay = 10;
        fixedDelay(delay);
        return delay;
    }

    @Override
    public long saveBatch(Iterable<?> objects) {
        if (Iterables.isEmpty(objects)) {
            return 0;
        }
        int delay = 15;
        fixedDelay(delay);
        return delay;
    }
}
