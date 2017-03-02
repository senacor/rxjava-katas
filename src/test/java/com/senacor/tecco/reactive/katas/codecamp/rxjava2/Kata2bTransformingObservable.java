package com.senacor.tecco.reactive.katas.codecamp.rxjava2;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.CountServiceImpl;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.services.WikiServiceImpl;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2bTransformingObservable {

    private WikiService wikiService = WikiService.create();
    private CountService countService = CountService.create();

    @Test
    public void transformingObservable() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the word count of the article to the console (ParsedPage.getText()). Use CountService.

        // wikiService.fetchArticleObservable()
    }

}
