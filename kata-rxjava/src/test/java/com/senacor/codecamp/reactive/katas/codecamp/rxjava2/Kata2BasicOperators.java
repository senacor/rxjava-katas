package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;

import de.tudarmstadt.ukp.wikipedia.parser.Paragraph;
import io.reactivex.Observable;

import com.senacor.codecamp.reactive.katas.KataClassification;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Andreas Keefer
 */
public class Kata2BasicOperators {

    private WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(mandatory)
    public void basicsA() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
    	String articleName = "Observable";
    	
    	
    	wikiService.fetchArticleObservable(articleName)
    	.map(wikiService::parseMediaWikiText)
    	.map((page) -> {
    		List<Paragraph> paragraphs = page.getParagraphs();
    		if (paragraphs.size() > 0) {
    			return paragraphs.get(0).getText();
    		}
    		
    		return "";
    	})
    	.subscribe(System.out::println);
    	
    	
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        //    and print out the first paragraph
    	
        // wikiService.fetchArticleObservable()
    }

    @Test
    @KataClassification(advanced)
    public void basicsB() throws Exception {
    	String articleName = "Observable";
    	
    	
    	
    	wikiService.fetchArticleObservable(articleName)
    	.map(wikiService::parseMediaWikiText)
    	.flatMapIterable((page) -> Arrays.asList(StringUtils.split(page.getText(), " ")))
    	.filter((word) -> {
    		return (word.length() > 0 && word.charAt(0) == 'a');
    	})
    	.map((word) -> word.length())
    	.reduce(0, (previous, current) -> previous + current)
    	.subscribe(System.out::println);
    	
        // 3. split the Article (ParsedPage.getText()) in words (separator=" ")
    	

        // 4. sum the number of letters of all words beginning with character 'a' to the console

        // wikiService.fetchArticleObservable()
    }

    @Test
    @KataClassification(hardcore)
    public void basicsC() throws Exception {
        // 5. filter out redundant words beginning with 'a'
String articleName = "Observable";
    	
    	
    	
    	wikiService.fetchArticleObservable(articleName)
    	.map(wikiService::parseMediaWikiText)
    	.flatMapIterable((page) -> Arrays.asList(StringUtils.split(page.getText(), " ")))
    	.filter((word) -> {
    		return (word.length() > 0 && word.charAt(0) == 'a');
    	})
    	.distinct()
    	.sorted((word1, word2) -> {
    		return word1.length() - word2.length();
    	});
        // 6. order them by length and take only the top 10 words in length

        //  wikiService.fetchArticleObservable()
    }
}
