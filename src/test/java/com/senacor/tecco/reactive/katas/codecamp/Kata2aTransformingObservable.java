package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2aTransformingObservable {

    private WikiService wikiService = new WikiService();

    @Test
    public void transformingObservable() throws Exception {

        String article = "Boeing 777";

        wikiService.fetchArticleObservable(article)
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
                .flatMap(wikiService::parseMediaWikiTextObservable)
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the text of the wikipedia article to the console (ParsedPage.getText())
                .subscribe(
                        ParsedPage::getText,
                        Throwable::printStackTrace,
                        System.out::println
                );
    }

}
