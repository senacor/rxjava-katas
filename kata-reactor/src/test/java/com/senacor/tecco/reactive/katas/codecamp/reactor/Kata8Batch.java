package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.katas.KataClassification;
import com.senacor.tecco.reactive.services.PersistService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import static com.senacor.tecco.reactive.katas.KataClassification.Classification.advanced;
import static com.senacor.tecco.reactive.katas.KataClassification.Classification.mandatory;

/**
 * @author Andreas Keefer
 */
public class Kata8Batch {

    private final WikiService wikiService = WikiService.create();
    private final PersistService persistService = PersistService.create();

    /**
     * 1. use {@link WikiService#wikiArticleBeingReadFluxBurst()} that returns a stream of wiki article being read
     * 2. watch the stream 2 sec
     * 3. save the article ({@link PersistService#save(String)}). The service returns the execution time
     * 4. sum the execution time of the service calls and print the result
     */
    @Test
    @KataClassification(mandatory)
    public void withoutBatch() throws Exception {
        wikiService.wikiArticleBeingReadFluxBurst();
    }

    @Test
    @KataClassification(mandatory)
    public void batch() throws Exception {
        // 1. do the same as above, but this time use the method #save(Iterable) to save a batch of articles.
        //    use a batch size of 5.
        //    Please note that this is a stream - you can not wait until all articles are delivered to save everything in a batch

        wikiService.wikiArticleBeingReadFluxBurst();
    }

    @Test
    @KataClassification(advanced)
    public void batch2() throws Exception {
        // 2. If the batch size is not reached within 500 milliseconds,
        //    flush the buffer anyway by writing to the service

        wikiService.wikiArticleBeingReadFluxBurst();
    }
}