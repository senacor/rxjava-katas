package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.services.integration.BlackHoleDatabase;
import com.senacor.tecco.reactive.services.integration.BlackHoleDatabaseImpl;
import com.senacor.tecco.reactive.util.*;

public class PersistService {

    private final BlackHoleDatabase database;

    public static PersistService create() {
        return create(DelayFunction.withNoDelay(),
                FlakinessFunction.noFlakiness());
    }

    public static PersistService create(DelayFunction delayFunction) {
        return create(delayFunction, FlakinessFunction.noFlakiness());
    }

    public static PersistService create(FlakinessFunction flakinessFunction) {
        return create(DelayFunction.withNoDelay(), flakinessFunction);
    }

    public static PersistService create(DelayFunction delayFunction,
                                        FlakinessFunction flakinessFunction) {
        return new PersistService(flakinessFunction, delayFunction);
    }

    private PersistService(FlakinessFunction flakinessFunction, DelayFunction delayFunction) {
        database = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new BlackHoleDatabaseImpl(), flakinessFunction)
                        , delayFunction));
    }

    /**
     * @param wikiArticle article
     * @return runtime
     */
    public long save(String wikiArticle) {
        return database.save(wikiArticle);
    }

    /**
     * @param wikiArticle article
     * @return runtime
     */
    public long save(Iterable<String> wikiArticle) {
        return database.save(wikiArticle);
    }


}
