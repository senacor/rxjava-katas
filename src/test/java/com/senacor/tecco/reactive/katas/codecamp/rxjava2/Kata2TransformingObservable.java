package com.senacor.tecco.reactive.katas.codecamp.rxjava2;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

    private WikiService wikiService = WikiService.create();
    private CountService countService = CountService.create();

    /**
     * 1. Use the {@link WikiService#fetchArticleObservable} and fetch an arbitrary wikipedia article
     * 2. transform the result with the {@link WikiService#parseMediaWikiText} to an object structure
     * 3. print the text of the wikipedia article to the console {@link ParsedPage#getText()}
     */
    @Test
    public void transformingA() throws Exception {
        // wikiService.fetchArticleObservable()
    }

    @Test
    public void transformingB() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the word count of the article to the console (ParsedPage.getText()). Use CountService.

        // wikiService.fetchArticleObservable()
    }

    @Test
    public void transformingC() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the number of words that begin with character 'a' to the console (ParsedPage.getText())

        // wikiService.fetchArticleObservable()
    }
}
