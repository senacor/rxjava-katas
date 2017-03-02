package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.*;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;
import static com.senacor.tecco.reactive.ReactiveUtil.print;

public class PersistServiceImpl implements PersistService {

    PersistServiceImpl() {
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
