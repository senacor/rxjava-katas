package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Random;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    private final WikiService wikiService = WikiService.create();

    /**
     * 1. use {@link Kata7ErrorHandling#fetchArticleFluxWithRandomErrors(String)} which randomly has a Timeout (ERROR).
     * 2. handle error: use retries with increasing delays
     * 3. if retries fail, use a default.
     * 4. parse article with {@link WikiService#parseMediaWikiText(String)}
     * 5. print {@link ParsedPage#getText()} to the console
     * <p>
     * HINT: To test your retry/default behavior you can use {@link Flux#error(Throwable)}
     */
    @Test
    public void errors() throws Exception {

        fetchArticleFluxWithRandomErrors("42");
    }

    private Flux<String> fetchArticleFluxWithRandomErrors(String articleName) {
        final Random randomGenerator = new Random(12L);
        return wikiService.fetchArticleFlux(articleName)
                .map(article -> {
                    if (randomGenerator.nextInt() % 2 == 0) {
                        throw new IllegalStateException("timeout");
                    }
                    return article;
                });
    }
}
