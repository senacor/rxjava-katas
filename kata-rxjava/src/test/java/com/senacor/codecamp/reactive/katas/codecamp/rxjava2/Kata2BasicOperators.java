package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.swing.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;
import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata2BasicOperators {

    private WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(mandatory)
    public void basicsA() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        wikiService.fetchArticleObservable("Hamburger")
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
            .map(wikiService::parseMediaWikiText)
            .map(parsedPage -> parsedPage.getFirstParagraph().getText())
            .subscribe(next -> System.out.print(next));
        //    and print out the first paragraph
        // wikiService.fetchArticleObservable()
    }

    @Test
    @KataClassification(advanced)
    public void basicsB() throws Exception {
        // 3. split the Article (ParsedPage.getText()) in words (separator=" ")
        wikiService.fetchArticleObservable("Hamburger")
                .map(wikiService::parseMediaWikiText)
                .flatMapIterable(parsedPage -> Arrays.asList(StringUtils.split(parsedPage.getText(), " ")))
                //.flatMap(parsedPage -> Observable.from(StringUtils.split(parsedPage.getText(), " ")))
                .filter(word -> word.startsWith("a"))
                .doOnNext(next -> print("words starting with 'a': %s", next))
                .reduce(0, (letterCount, word) -> letterCount + word.length())
                .doOnSuccess(next -> print("letter count of 'a'-words: %s", next))
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValue(value -> value > 1000)
                .assertComplete();
        // 4. sum the number of letters of all words beginning with character 'a' to the console

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
