package com.senacor.tecco.reactive.services;

import static com.senacor.tecco.reactive.ReactiveUtil.fixedDelay;
import static com.senacor.tecco.reactive.ReactiveUtil.print;

public class PersistService {

    /**
     * @param wikiArticle article
     * @return runtime
     */
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
    public long save(Iterable<String> wikiArticle) {
        print("save(%s)", wikiArticle);
        int delay = 15;
        fixedDelay(delay);
        return delay;
    }
}
