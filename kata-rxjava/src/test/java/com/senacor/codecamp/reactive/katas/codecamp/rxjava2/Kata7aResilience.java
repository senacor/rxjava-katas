package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import com.senacor.codecamp.reactive.katas.KataClassification;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata7aResilience {

    @Test
    @KataClassification(mandatory)
    public void timeout() throws Exception {
        // 1. use fetchArticleObservableWithTimeout and add there a timeout of 500ms.
        // 2. verify this behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1000));

        fetchArticleObservableWithTimeout(wikiService, "42")
        .timeout(500, TimeUnit.MILLISECONDS)
        .test()
        .assertError(Exception.class);
        
        wikiService = WikiService.create(DelayFunction.staticDelay(400));

        fetchArticleObservableWithTimeout(wikiService, "42")
        .timeout(500, TimeUnit.MILLISECONDS)
        .test()
        .assertNoErrors();
    }

    private Observable<String> fetchArticleObservableWithTimeout(WikiService wikiService, String articleName) {
        return wikiService.fetchArticleObservable(articleName);
    }

    @Test
    @KataClassification(hardcore)
    public void retry() throws Exception {
        // 3. when fetchArticleObservableWithTimeout fails, retry twice with a delay of 1 second
        // 4. verify this behavior with tests
    	
    	long retries = 2;

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(400),
                FlakinessFunction.failCountDown(1));

        fetchArticleObservableWithTimeout(wikiService, "42")
        .retry((no, err) -> {
        	if (no <= retries) {
        		Thread.sleep(1000);
        		return true;
        	}
        	return false;
        })
        .test()
        .assertNoErrors();
        
        wikiService = WikiService.create(DelayFunction.staticDelay(400),
                FlakinessFunction.failCountDown(2));

        fetchArticleObservableWithTimeout(wikiService, "42")
        .retry((no, err) -> {
        	if (no <= retries) {
        		Thread.sleep(1000);
        		return true;
        	}
        	return false;
        })
        .test()
        .assertNoErrors();
        
        wikiService = WikiService.create(DelayFunction.staticDelay(400),
                FlakinessFunction.failCountDown(3));

        fetchArticleObservableWithTimeout(wikiService, "42")
        .retry((no, err) -> {
        	if (no <= retries) {
        		Thread.sleep(1000);
        		return true;
        	}
        	return false;
        })
        .test()
        .assertError(Exception.class);
    }

    @Test
    @KataClassification(nightmare)
    public void ambiguous() throws Exception {
        // 5. We can do better! Take a look at the amb() operator to beat the “flakiness” and speed up
        //    fetching articles.

        WikiService wikiService = WikiService.create(DelayFunction.withRandomDelay(200, 1000));
        
        WaitMonitor monitor = new WaitMonitor();

        Observable.ambArray(
        		fetchArticleObservableWithTimeout(wikiService, "42").subscribeOn(Schedulers.io())
        		)
        .doOnComplete(() -> monitor.complete())
        .test()
        .assertComplete();
        
        monitor.waitFor(10, TimeUnit.SECONDS);
        
        Observable.ambArray(
        		fetchArticleObservableWithTimeout(wikiService, "42").subscribeOn(Schedulers.io()),
        		fetchArticleObservableWithTimeout(wikiService, "42").subscribeOn(Schedulers.io()),
        		fetchArticleObservableWithTimeout(wikiService, "42").subscribeOn(Schedulers.io())
        		)
        .doOnComplete(() -> monitor.complete())
        .test()
        .assertComplete();
        
        monitor.waitFor(10, TimeUnit.SECONDS);
    }
}
