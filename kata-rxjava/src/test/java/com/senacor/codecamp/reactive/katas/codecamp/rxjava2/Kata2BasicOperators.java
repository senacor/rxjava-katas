package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import org.junit.Test;

import io.reactivex.Observable;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;
import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Andreas Keefer
 */
public class Kata2BasicOperators {

    private WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(mandatory)
    public void basicsA() throws Exception {
        wikiService.fetchArticleObservable("Schnitzel")
                .map((article) -> wikiService.parseMediaWikiText(article))
                .map((text) -> text.getParagraph(1))
                .map((text) -> text.getText())
                .subscribe(next -> {
                    // print and assert the emitted value in the "onNext" Handler
                    print("next: %s", next);
                },
                error -> {
                    // handle Errors in the "onError" Handler
                    error.printStackTrace();
                    fail("No Error expected");
                },
                () -> {
                    // onComplete Handler
                    print("complete!");
                    // trigger waitMonitor.complete() when execution ended as expected
                });

        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        //    and print out the first paragraph

        // wikiService.fetchArticleObservable()
    }

    @Test
    @KataClassification(advanced)
    public void basicsB() throws Exception {
        // 3. split the Article (ParsedPage.getText()) in words (separator=" ")
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
