package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata2bTransformingObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();

    @Test
    public void transformingObservable() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the word count of the article to the console (ParsedPage.getText()). Use CountService.

        // wikiService.fetchArticleObservable()

        final String articleName = "Flugzeug";
        Observable<String> article = wikiService.fetchArticleObservable(articleName);
        article.map(wikiService::parseMediaWikiText)
                .map(countService::countWords)
                .subscribe(wordCount -> System.out.println("The article " + articleName + " contains " + wordCount + " words."));
    }

}
