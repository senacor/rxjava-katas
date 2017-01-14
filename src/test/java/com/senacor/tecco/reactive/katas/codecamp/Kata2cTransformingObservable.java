package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

import java.util.Arrays;

/**
 * @author Andreas Keefer
 */
public class Kata2cTransformingObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();

    @Test
    public void transformingObservable() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the number of words that begin with character 'a' to the console (ParsedPage.getText())

        // wikiService.fetchArticleObservable()


        final String articleName = "Flugzeug";
        Observable<String> article = wikiService.fetchArticleObservable(articleName);
        article.map(wikiService::parseMediaWikiText)
                .map(parsedPage -> parsedPage.getText())
                .map(parsedText -> parsedText.split(" "))
                //.flatMap(words -> Observable.from(words))
                .flatMapIterable(words -> Arrays.asList(words))
                .filter(word -> word.startsWith("a"))
                .count()
                .subscribe(aWordCount-> System.out.println("The article " + articleName + " contains " + aWordCount + " words starting with 'a'."));
    }
}
