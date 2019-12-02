package com.senacor.codecamp.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class Kata3Combining {

    private final WikiService wikiService = WikiService.create();
    private final RatingService ratingService = RatingService.create();
    private final CountService countService = CountService.create();

    @Test
    public void combiningObservable() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and #countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        wikiService.fetchArticleObservable(wikiArticle)
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> {
                    Observable<Integer> rating = ratingService.rateObservable(parsedPage);
                    Observable<Integer> wordCount = countService.countWordsObservable(parsedPage);
                    return Observable.zip(rating, wordCount, (r, wc) -> String.format(
                            "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                            wikiArticle, r, wc));
                })
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        waitMonitor::complete);

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
        assertThat(waitMonitor.isComplete(), is(true));
    }

    @Test
    public void combiningObservablePublish() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and #countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        ConnectableObservable<ParsedPage> parsedPageObservable = wikiService.fetchArticleObservable(wikiArticle)
                // a scheduler is necessary for some reason now in RxJava 2 to get the
                // ConnectableObservable working as expected
                .subscribeOn(Schedulers.io())
                .flatMap(wikiService::parseMediaWikiTextObservable).publish();

        Observable<Integer> ratingObservable = parsedPageObservable.flatMap(ratingService::rateObservable);
        Observable<Integer> wordCountObservable = parsedPageObservable.flatMap(countService::countWordsObservable);
        parsedPageObservable.connect();

        Observable.zip(ratingObservable, wordCountObservable, (r, wc) -> String.format(
                "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                wikiArticle, r, wc))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        waitMonitor::complete);

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
        assertThat(waitMonitor.isComplete(), is(true));
    }

}
