package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.katas.KataClassification;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import com.senacor.tecco.reactive.util.FlakinessFunction;
import org.junit.Test;
import reactor.core.publisher.Flux;

import static com.senacor.tecco.reactive.katas.KataClassification.Classification.*;

/**
 * @author Andreas Keefer
 */
public class Kata7aResilience {

    @Test
    @KataClassification(mandatory)
    public void timeout() throws Exception {
        // 1. use fetchArticleFluxWithTimeout and add there a timeout of 500ms.
        // 2. verify this behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1000));

        fetchArticleFluxWithTimeout(wikiService, "42");
    }

    private Flux<String> fetchArticleFluxWithTimeout(WikiService wikiService, String articleName) {
        return wikiService.fetchArticleFlux(articleName);
    }

    @Test
    @KataClassification(hardcore)
    public void retry() throws Exception {
        // 3. when fetchArticleFluxWithTimeout fails, retry twice with a delay of 1 second
        // 4. verify this behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(400),
                FlakinessFunction.failCountDown(1));

        fetchArticleFluxWithTimeout(wikiService, "42");
    }

    @Test
    @KataClassification(nightmare)
    public void ambiguous() throws Exception {
        // 5. We can do better! Take a look at the amb() operator to beat the “flakiness” and speed up
        //    fetching articles.

        WikiService wikiService = WikiService.create(DelayFunction.withRandomDelay(200, 1000));

        fetchArticleFluxWithTimeout(wikiService, "42");
    }
}
