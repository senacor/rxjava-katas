package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.PersistService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata8Batch {

    private final WikiService wikiService = new WikiService();
    private final PersistService persistService = new PersistService();

    /**
     * 1. use {@link WikiService#wikiArticleBeingReadFluxBurst()} that returns a stream of wiki article being read
     * 2. watch the stream 2 sec
     * 3. save the article ({@link PersistService#save(String)}). The service returns the execution time
     * 4. sum the execution time of the service calls and print the result
     */
    @Test
    public void withoutBatch() throws Exception {
        wikiService.wikiArticleBeingReadFluxBurst();
    }

    /**
     * 1. do the same as above, but this time use the method ({@link PersistService#save(Iterable)}) to save a
     * batch of articles. Please note  that this is a stream - you can not wait until all articles are
     * delivered to save everything in a batch
     */
    @Test
    public void batch() throws Exception {
        wikiService.wikiArticleBeingReadFluxBurst();
    }
}