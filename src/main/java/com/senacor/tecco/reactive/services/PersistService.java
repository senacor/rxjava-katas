package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.*;

/**
 * @author Andreas Keefer
 */
public interface PersistService {

    static PersistService create() {
        return create(DelayFunction.withNoDelay(),
                FlakinessFunction.noFlakiness());
    }

    static PersistService create(DelayFunction delayFunction) {
        return create(delayFunction, FlakinessFunction.noFlakiness());
    }

    static PersistService create(FlakinessFunction flakinessFunction) {
        return create(DelayFunction.withNoDelay(), flakinessFunction);
    }

    static PersistService create(DelayFunction delayFunction,
                                        FlakinessFunction flakinessFunction) {
        return StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new PersistServiceImpl(), flakinessFunction)
                        , delayFunction));
    }

    long save(String wikiArticle);

    long save(Iterable<String> wikiArticle);
}
