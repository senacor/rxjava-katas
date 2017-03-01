package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.*;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;
import static com.senacor.tecco.reactive.ReactiveUtil.print;

public class PersistServiceImpl implements PersistService {

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
        return StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new PersistServiceImpl(), flakinessFunction)
                        , delayFunction));
    }

    private PersistServiceImpl() {
    }

    /**
     * @param wikiArticle article
     * @return runtime
     */
    @Override
    public long save(String wikiArticle) {
        print("save(%s)", wikiArticle);
        int delay = 10;
        fixedDelay(delay);
        return delay;
    }

    /**
     * @param wikiArticle article
     * @return runtime
     */
    @Override
    public long save(Iterable<String> wikiArticle) {
        print("save(%s)", wikiArticle);
        int delay = 15;
        fixedDelay(delay);
        return delay;
    }
}
