package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.DelayFunction;
import com.senacor.tecco.reactive.util.FlakinessFunction;

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
        return new PersistServiceImpl(flakinessFunction, delayFunction);
    }

    /**
     * @param wikiArticle article
     * @return runtime
     */
    long save(String wikiArticle);

    /**
     * @param wikiArticle article
     * @return runtime
     */
    long save(Iterable<String> wikiArticle);
}
