package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata2cTransformingObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();

    @Test
    public void transformingObservable() throws Exception {
        String article = "Boeing 777";
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        wikiService.fetchArticleObservable(article)
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
            .flatMap(wikiService::parseMediaWikiTextObservable)
        // 3. print the number of words that begin with character 'a' to the console (ParsedPage.getText())
            .map(x -> x.getText())
                .flatMap ( x -> {
                    String[] parts = x.split(" ");
                    return Observable.from(parts);
                })
                .filter(x -> x.startsWith("a"))
                .count()
                .subscribe(
                        x -> System.out.println(x),
                        Throwable::printStackTrace,
                        () -> System.out.println("done")
                );
    }

}
