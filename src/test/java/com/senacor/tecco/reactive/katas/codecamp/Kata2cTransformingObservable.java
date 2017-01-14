package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;

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

        wikiService.fetchArticleObservable("Boeing 777")
            .map(article -> wikiService.parseMediaWikiText(article))
            .map(ParsedPage::getText)
            .flatMap(text -> {

                ArrayList<String> words = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(text, " ");

                while (tokenizer.hasMoreTokens()) {
                   words.add(tokenizer.nextToken());
                }

                return Observable.from(words);
            })
            .filter(text -> text.startsWith("a"))
            .count()
            .subscribe(
                    n -> System.out.println("Count words with a: " + n),
                    err -> System.err.println("Error: " + err.getMessage()),
                    () -> System.out.println("Done")
            );
    }

}
