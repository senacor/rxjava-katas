package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;

import io.reactivex.Observable;

import com.senacor.codecamp.reactive.katas.KataClassification;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;

/**
 * @author Andreas Keefer
 */
public class Kata2BasicOperators {

    private WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(mandatory)
    public void basicsA() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        //    and print out the first paragraph

    	wikiService.fetchArticleObservable("Observable")
    		.map(wikiService::parseMediaWikiText)
    		.map(a -> a.getFirstParagraph())
    		.map(p -> p.getText())
    	.subscribe(System.out::println); 
    }

    @Test
    @KataClassification(advanced)
    public void basicsB() throws Exception {
        // 3. split the Article (ParsedPage.getText()) in words (separator=" ")
        // 4. sum the number of letters of all words beginning with character 'a' to the console
    	
    	wikiService.fetchArticleObservable("Observable")
    		.map(wikiService::parseMediaWikiText)
    		.map(parsedPage -> parsedPage.getText().split(" "))
    		.flatMap(s -> Observable.fromArray(s))
    		.filter(s -> s.startsWith("a"))
    		.count()
    	.subscribe(); 

        // wikiService.fetchArticleObservable()
    }

    @Test
    @KataClassification(hardcore)
    public void basicsC() throws Exception {
        // 5. filter out redundant words beginning with 'a'
        // 6. order them by length and take only the top 10 words in length

        //  wikiService.fetchArticleObservable()
    }
}
