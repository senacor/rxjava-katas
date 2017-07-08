package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

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

        wikiService.fetchArticleObservable("42")
                .onErrorResumeNext(wikiServiceBackup.fetchArticleObservable("42").subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValueCount(1)
                .assertComplete();
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

        String articleName = "42";
        wikiService.fetchArticleObservable(articleName)
                .onErrorResumeNext(wikiServiceBackup.fetchArticleObservable(articleName).subscribeOn(Schedulers.io()))
                .onErrorReturn(error -> getCachedArticle(articleName))
                .subscribeOn(Schedulers.io())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValueCount(1)
                .assertValue("{{Dieser Artikel|behandelt " + articleName + "}} ")
                .assertComplete();
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
        WaitMonitor waitMonitor = new WaitMonitor();

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(400),
                FlakinessFunction.failCountDown(4));

        wikiService.fetchArticleObservable("42")
                .retryWhen(attempts -> attempts
                        .zipWith(Observable.range(1, 5), (n, i) -> i)
                                .flatMap(i -> Observable.timer(i^2 * 100, TimeUnit.MILLISECONDS))
                )
                .subscribeOn(Schedulers.io())
                .test()
                .awaitDone(10, TimeUnit.SECONDS)
                .assertValueCount(1);

    }
}
