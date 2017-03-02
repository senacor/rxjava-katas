package com.senacor.tecco.reactive.services.integration;

import org.apache.commons.lang.StringUtils;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;
import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class BlackHoleDatabaseImpl implements BlackHoleDatabase {

    @Override
    public long save(Object object) {
        print("save(%s)", StringUtils.abbreviate(object.toString(), 50));
        int delay = 10;
        fixedDelay(delay);
        return delay;
    }

    @Override
    public long save(Iterable<Object> objects) {
        print("save(%s)", StringUtils.abbreviate(objects.toString(), 50));
        int delay = 15;
        fixedDelay(delay);
        return delay;
    }
}
