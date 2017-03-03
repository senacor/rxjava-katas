package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.Arrays;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata2Transforming {

    private final WikiService wikiService = WikiService.create();
    private final CountService countService = CountService.create();

    /**
     * 1. Use the {@link WikiService#fetchArticleFlux(String)} and fetch an arbitrary wikipedia article
     * 2. transform the result with the {@link WikiService#parseMediaWikiText} to an object structure
     * 3. print the text of the wikipedia article to the console ({@link ParsedPage#getText})
     */
    @Test
    public void transforming() throws Exception {
        wikiService.fetchArticleFlux("Bilbilis")
//                   .log()
                .flatMap(wikiService::parseMediaWikiTextFlux)
//                   .log()
                .flatMapIterable(parsedPage -> Arrays.asList(parsedPage.getText().split(" ")))
                .filter(word -> word.startsWith("a"))
                .subscribe(next -> print("next: %s", next), Throwable::printStackTrace);
    }
}