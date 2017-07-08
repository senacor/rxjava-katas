package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import com.senacor.codecamp.reactive.katas.KataClassification;
import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Andreas Keefer
 */
public class Kata7aResilience {
	
    WaitMonitor waitMonitor = new WaitMonitor();

//    @Test
    @KataClassification(mandatory)
    public void timeout() throws Exception {
        // 1. use fetchArticleObservableWithTimeout and add there a timeout of 500ms.
        // 2. verify this behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(1000));
//        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(400));

        fetchArticleObservableWithTimeout(wikiService, "42")
        	.test()
        	.await()
        	.assertError(TimeoutException.class); 
        	
//        	.assertNoTimeout(); 
        
//        waitMonitor.waitFor(10, TimeUnit.SECONDS);
        
    }

    private Observable<String> fetchArticleObservableWithTimeout(WikiService wikiService, String articleName) {
    	return wikiService.fetchArticleObservable(articleName).timeout(500, TimeUnit.MILLISECONDS); 
    }

    @Test
    @KataClassification(hardcore)
    public void retry() throws Exception {
        // 3. when fetchArticleObservableWithTimeout fails, retry twice with a delay of 1 second
        // 4. verify this behavior with tests

        WikiService wikiService = WikiService.create(DelayFunction.staticDelay(600),
                FlakinessFunction.failCountDown(1));

        fetchArticleObservableWithTimeout(wikiService, "42")
//        	.retry(2)
        	.retryWhen(attempts -> {
        		return attempts.zipWith(Observable.range(1, 2), (n, i) -> i).flatMap(i -> {
        			System.out.println("delay retry by " + i + " second(s)");
        			return Observable.timer(i, TimeUnit.SECONDS); 
        		}); 
        	})
        	.test()
        	.awaitDone(5, TimeUnit.SECONDS)
        	.assertError(TimeoutException.class); 
    }

    @Test
    @KataClassification(nightmare)
    public void ambiguous() throws Exception {
        // 5. We can do better! Take a look at the amb() operator to beat the “flakiness” and speed up
        //    fetching articles.

        WikiService wikiService = WikiService.create(DelayFunction.withRandomDelay(200, 1000));

        fetchArticleObservableWithTimeout(wikiService, "42");
        
    }
}
