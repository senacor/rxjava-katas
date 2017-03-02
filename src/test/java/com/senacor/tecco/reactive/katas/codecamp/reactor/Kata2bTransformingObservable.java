package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.CountServiceImpl;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2bTransformingObservable {

    private WikiService wikiService = WikiService.create();
    private CountService countService = CountService.create();

    /**
     * 1. Use the {@link WikiService#fetchArticleObservable} and fetch an arbitrary wikipedia article
     * 2. transform the result with the {@link WikiService#parseMediaWikiText} to an object structure
     * 3. print the word count of the article to the console ({@link ParsedPage#getText}). Use CountService.
     */
    @Test
    public void transformingObservable() throws Exception {
        wikiService.fetchArticleObservable("Bilbilis");
    }
}