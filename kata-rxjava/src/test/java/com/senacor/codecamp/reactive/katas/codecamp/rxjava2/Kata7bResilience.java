package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;
import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata7bResilience {

    @Test
    @KataClassification(mandatory)
    public void backupOnError() throws Exception {
        // 1. use 'wikiService.fetchArticleObservable' to fetch an article. This Service always fails
        // 2. switch to the 'wikiServiceBackup.fetchArticleObservable' when the wikiService fails
        // 3. verify the behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(10),
                FlakinessFunction.alwaysFail());
        WikiService wikiServiceBackup = WikiService.create(DelayFunction.staticDelay(100));

        wikiService.fetchArticleObservable("42");
    }

    @Test
    @KataClassification(advanced)
    public void defaultValueBackup() throws Exception {
        // 4. if the call to the 'wikiServiceBackup' also fails, return a default value (e.g. 'getCachedArticle')
        // 5. verify the behavior with tests


        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(10),
                FlakinessFunction.alwaysFail());
        WikiService wikiServiceBackup = WikiService.create(DelayFunction.staticDelay(100),
                FlakinessFunction.alwaysFail());

        wikiService.fetchArticleObservable("42");
    }

    private String getCachedArticle(String articleName) {
        print("getCachedArticle: '%s'", articleName);
        return "{{Dieser Artikel|behandelt " + articleName + "}} ";
    }

    @Test
    @KataClassification(hardcore)
    public void exponentialRetry() throws Exception {
        // 6. insert in this example a retry strategy: 3 retries with an exponential back-off
        //    (e.g wait 100ms for the first retry, 400ms for the second retry and 900ms for the 3rd retry)
    }
}
