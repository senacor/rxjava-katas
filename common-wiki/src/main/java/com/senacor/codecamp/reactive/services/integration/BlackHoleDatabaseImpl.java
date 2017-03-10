package com.senacor.codecamp.reactive.services.integration;

import com.google.common.collect.Iterables;
import com.senacor.codecamp.reactive.util.ReactiveUtil;

/**
 * @author Andreas Keefer
 */
public class BlackHoleDatabaseImpl implements BlackHoleDatabase {

    @Override
    public long saveOne(Object object) {
        int delay = 10;
        ReactiveUtil.fixedDelay(delay);
        return delay;
    }

    @Override
    public long saveBatch(Iterable<?> objects) {
        if (Iterables.isEmpty(objects)) {
            return 0;
        }
        int delay = 15;
        ReactiveUtil.fixedDelay(delay);
        return delay;
    }
}
