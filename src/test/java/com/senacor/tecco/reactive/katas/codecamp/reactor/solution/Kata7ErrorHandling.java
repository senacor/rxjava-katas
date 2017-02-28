package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

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


        final WaitMonitor monitor = new WaitMonitor();
        final String articleName = "42";
        Disposable subscription = fetchArticleObservableWithRandomErrors(articleName)
                // delayed retry
                .retryWhen(retryWithDelay(3))
                // default on error
                .onErrorReturn(getCachedArticle(articleName))
                .map(wikiService::parseMediaWikiText)
                .subscribe(next -> print("next: %s", next.getText()),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(20, TimeUnit.SECONDS);
        subscription.dispose();
    }

    @Test
    public void errorsWithDefaultTest() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        final String articleName = "42";
        Disposable subscription = Flux.<String>error(new IllegalStateException("timeout"))
                // delayed retry
                .retryWhen(retryWithDelay(3))
                // default on error
                .onErrorReturn(getCachedArticle(articleName))
                .map(wikiService::parseMediaWikiText)
                .subscribe(next -> print("next: %s", next.getText()),
                        Throwable::printStackTrace,
                        () -> {
                            print("complete!");
                            monitor.complete();
                        });

        monitor.waitFor(20, TimeUnit.SECONDS);
        subscription.dispose();
    }

    private String getCachedArticle(String articleName) {
        print("getCachedArticle: '%s'", articleName);
        return "{{Dieser Artikel|behandelt das Jahr 42}} ";
    }

    private Function<Flux<Throwable>, ? extends Publisher<?>> retryWithDelay(final int maxRetries) {
        return attempts -> attempts.zipWith(Flux.range(1, maxRetries + 1), ErrorWithRetryCount::new)
                                   .flatMap(
                                           countAndError -> {
                                               if (countAndError.getRetryCount() > maxRetries) {
                                                   throw Exceptions.propagate(countAndError.getThrowable());
                                               }
                                               print("randomDelay retry by %s second(s)", countAndError
                                                       .getRetryCount());
                                               Duration d = Duration
                                                       .of(countAndError.getRetryCount(), ChronoUnit.SECONDS);
                                               return Flux.interval(d).take(1);
                                           });
    }

    private static class ErrorWithRetryCount {
        private final Throwable throwable;
        private final int retryCount;

        private ErrorWithRetryCount(Throwable throwable, int retryCount) {
            this.throwable = throwable;
            this.retryCount = retryCount;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public int getRetryCount() {
            return retryCount;
        }
    }

    private Flux<String> fetchArticleObservableWithRandomErrors(String articleName) {
        final Random randomGenerator = new Random();
        return wikiService.fetchArticleFlux(articleName)
                          .map(article -> {
                              if (randomGenerator.nextInt() % 2 == 0) {
                                  throw new IllegalStateException("timeout");
                              }
                              return article;
                          });
    }
}
