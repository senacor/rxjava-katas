package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.services.integration.BlackHoleDatabase;
import com.senacor.tecco.reactive.services.integration.BlackHoleDatabaseImpl;
import com.senacor.tecco.reactive.util.*;

public class PersistServiceImpl implements PersistService {

    private final BlackHoleDatabase database;

    PersistServiceImpl(FlakinessFunction flakinessFunction, DelayFunction delayFunction) {
        database = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new BlackHoleDatabaseImpl(), flakinessFunction)
                        , delayFunction));
    }

    @Override
    public long save(String wikiArticle) {
        return database.save(wikiArticle);
    }

    @Override
    public long save(Iterable<String> wikiArticle) {
        return database.save(wikiArticle);
    }


}
