package com.senacor.tecco.reactive.katas.codecamp.rxjava2;

import com.senacor.tecco.reactive.services.WikiService;
import io.reactivex.Observable;
import org.junit.Test;

import java.util.Random;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    private final WikiService wikiService = new WikiService();

    @Test
    public void errors() throws Exception {
        // 1. use fetchArticleObservableWithRandomErrors which randomly has a Timeout (ERROR).
        // 2. handle error: use retries with increasing delays
        // 3. if retries fail, use a default.
        // 4. parse article with wikiService.parseMediaWikiText
        // 5. print parsedPage.getText to the console
        //
        // HINT: To test your retry/default behavior you can use Observable.error()

        fetchArticleObservableWithRandomErrors("42");
    }

    private Observable<String> fetchArticleObservableWithRandomErrors(String articleName) {
        final Random randomGenerator = new Random(12L);
        return wikiService.fetchArticleObservable(articleName).map(article -> {
            if (randomGenerator.nextInt() % 2 == 0) {
                throw new IllegalStateException("timeout");
            }
            return article;
        });
    }
}
