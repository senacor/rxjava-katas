package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2Transforming {

    private WikiService wikiService = WikiService.create();
    private CountService countService = CountService.create();

    /**
     * 1. Use the {@link WikiService#fetchArticleFlux} and fetch an arbitrary wikipedia article
     * 2. transform the result with the {@link WikiService#parseMediaWikiText} to an object structure
     * 3. print the text of the wikipedia article to the console ({@link ParsedPage#getText})
     */
    @Test
    public void transformingA() throws Exception {
        wikiService.fetchArticleFlux("Bilbilis");
    }

    /**
     * 1. Use the {@link WikiService#fetchArticleFlux} and fetch an arbitrary wikipedia article
     * 2. transform the result with the {@link WikiService#parseMediaWikiText} to an object structure
     * 3. print the word count of the article to the console ({@link ParsedPage#getText}). Use CountService.
     */
    @Test
    public void transformingB() throws Exception {
        wikiService.fetchArticleFlux("Bilbilis");
    }

    /**
     * 1. Use the {@link WikiService#fetchArticleFlux} and fetch an arbitrary wikipedia article
     * 2. transform the result with the {@link WikiService#parseMediaWikiText} to an object structure
     * 3. print the number of words that begin with character 'a' to the console ({@link ParsedPage#getText}).
     */
    @Test
    public void transformingC() throws Exception {
        wikiService.fetchArticleFlux("Bilbilis");
    }
}