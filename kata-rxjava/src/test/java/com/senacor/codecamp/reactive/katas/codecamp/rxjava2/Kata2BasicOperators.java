package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ContentElement;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
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
        Observable.fromCallable(() -> wikiService.fetchArticle("Observable"))
                .map(wikiService::parseMediaWikiText)
                .map(ParsedPage::getFirstParagraph)
                .map(ContentElement::getText)
                .subscribe(
                        System.out::println,
                        (error) -> System.err.println(error.getLocalizedMessage()),
                        () -> System.out.println("Completed")
                );
    }

    @Test
    @KataClassification(advanced)
    public void basicsB() throws Exception {
        wikiService.fetchArticleObservable("Observable")
                .map(wikiService::parseMediaWikiText)
                .map(ParsedPage::getText)
                .flatMap(s -> Observable.fromArray(s.split(" ")))
                .map(String::toLowerCase)
                .filter(s -> s.startsWith("a"))
                .map(String::length)
                .reduce(0, (i,j) -> i+j)
                .subscribe(System.out::println);
        // 3. split the Article (ParsedPage.getText()) in words (separator=" ")
        // 4. sum the number of letters of all words beginning with character 'a' to the console

        // wikiService.fetchArticleObservable()
    }

    @Test
    @KataClassification(hardcore)
    public void basicsC() throws Exception {
        wikiService.fetchArticleObservable("Observable")
                .map(wikiService::parseMediaWikiText)
                .map(ParsedPage::getText)
                .flatMap(s -> Observable.fromArray(s.split(" ")))
                .map(String::toLowerCase)
                .filter(s -> s.startsWith("a"))
                .distinct()
                .sorted((f,s) -> s.length() - f.length())
                .take(10)
                .subscribe(System.out::println);

        // 5. filter out redundant words beginning with 'a'
        // 6. order them by length and take only the top 10 words in length

        //  wikiService.fetchArticleObservable()
    }
}
