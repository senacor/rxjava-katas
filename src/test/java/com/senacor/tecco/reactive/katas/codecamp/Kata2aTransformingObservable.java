package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata2aTransformingObservable {

    private WikiService wikiService = new WikiService();

    @Test
    public void transformingObservable() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the text of the wikipedia article to the console (ParsedPage.getText())

        // wikiService.fetchArticleObservable()
        final String articleName = "Flugzeug";
        Observable<String> article = wikiService.fetchArticleObservable(articleName);
        article.map(wikiService::parseMediaWikiText)
                .subscribe(parsedPage -> System.out.println(parsedPage.getText()));
    }

}
