package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2aTransforming {

    private WikiService wikiService = WikiService.create();

    /**
     * 1. Use the {@link WikiService#fetchArticleFlux} and fetch an arbitrary wikipedia article
     * 2. transform the result with the {@link WikiService#parseMediaWikiText} to an object structure
     * 3. print the text of the wikipedia article to the console ({@link ParsedPage#getText})
     */
    @Test
    public void transforming() throws Exception {
        wikiService.fetchArticleFlux("Bilbilis");
    }
}