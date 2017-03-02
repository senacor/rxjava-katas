package com.senacor.tecco.reactive.services.integration;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;

/**
 * @author Andreas Keefer
 */
public class BlackHoleDatabaseImpl implements BlackHoleDatabase {

    @Override
    public long save(Object object) {
        int delay = 10;
        fixedDelay(delay);
        return delay;
    }

    @Override
    public long save(Iterable<Object> objects) {
        int delay = 15;
        fixedDelay(delay);
        return delay;
    }
}
