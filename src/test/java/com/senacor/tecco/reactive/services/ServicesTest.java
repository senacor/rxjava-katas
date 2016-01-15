package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Andreas Keefer
 */
public class ServicesTest {

    private final WikiService wikiService = new WikiService("en");
    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Test
    public void testFetchArticle() throws Exception {
        String article = wikiService.fetchArticleObservable("42").toBlocking().first();
        System.out.println(article);
        assertNotNull(article);
    }

    @Test
    public void testParseMediaWikiText() throws Exception {
        ParsedPage parsedPage = wikiService.parseMediaWikiTextObservable("== Weblinks ==").toBlocking().first();
        assertNotNull(parsedPage);
        assertEquals(1, parsedPage.getSections().size());
        assertEquals("Weblinks", parsedPage.getSections().iterator().next().getText());
    }

    @Test
    public void testParseMediaWikiTextWithLink() throws Exception {
        ParsedPage parsedPage = getParseMediaWikiTextWithLink();
        assertNotNull(parsedPage);
        assertEquals(1, parsedPage.getSections().size());
        assertEquals("Weblinks 42", parsedPage.getSections().iterator().next().getText());
        assertEquals(1, parsedPage.getLinks().size());

    }

    private ParsedPage getParseMediaWikiTextWithLink() throws Exception {
        ParsedPage parsedPage = wikiService.parseMediaWikiTextObservable("== Weblinks ==\n [[42]]").toBlocking().first();
        return parsedPage;
    }

    @Test
    public void testWikiArticleBeingReadObservable() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Subscription subscription = wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .subscribe(article -> {
                            print(article);
                            assertNotNull(article);
                        },
                        error -> {
                        }, monitor::complete
                );
        monitor.waitFor(1, TimeUnit.SECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testWikiArticleBeingReadObservableComplex() throws Exception {

        final WaitMonitor monitor = new WaitMonitor();
        final Scheduler scheduler = Schedulers.from(Executors.newFixedThreadPool(5));
        Subscription subscription = wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .sample(200, TimeUnit.MILLISECONDS)
                .doOnNext(name -> print("=> working with " + name))
                .flatMap((wikiArticle) -> wikiService.fetchArticleObservable(wikiArticle)
                        .subscribeOn(scheduler)
                        .zipWith(Observable.just(wikiArticle), (text, name) -> new String[]{name, text}))
                .flatMap(array -> wikiService.parseMediaWikiTextObservable(array[1]).subscribeOn(Schedulers.computation())
                        .zipWith(Observable.just(array[0]), (parsedPage, name) -> new Object[]{name, parsedPage}))
                .flatMap(array -> {
                    final String articleName = (String) array[0];
                    final ParsedPage parsedPage = (ParsedPage) array[1];
                    Observable<String> zipjSON = Observable.zip(ratingService.rateObservable(parsedPage)
                            .subscribeOn(scheduler)
                            , countService.countWordsObervable(parsedPage)
                            .subscribeOn(scheduler)
                            , (rating, wordCount) -> String.format("{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                            articleName, rating, wordCount));
                    return zipjSON;
                })
                .subscribe(analizedArticle -> {
                            print(analizedArticle);
                            assertNotNull(analizedArticle);
                        },
                        error -> {
                        }, monitor::complete
                );
        monitor.waitFor(5, TimeUnit.SECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testRate() throws Exception {
        ParsedPage parsedPage = getParseMediaWikiTextWithLink();
        Integer rating = ratingService.rateObservable(parsedPage).toBlocking().first();
        assertEquals(5, rating.intValue());

        parsedPage = wikiService.parseMediaWikiTextObservable("== Weblinks ==\n[[42]] [[42]]").toBlocking().first();
        rating = ratingService.rateObservable(parsedPage).toBlocking().first();
        assertEquals(5, rating.intValue());

        parsedPage = wikiService.parseMediaWikiTextObservable("== Weblinks ==").toBlocking().first();
        rating = ratingService.rateObservable(parsedPage).toBlocking().first();
        assertEquals(0, rating.intValue());
    }

    @Test
    public void testCountWords() throws Exception {
        ParsedPage parsedPage = getParseMediaWikiTextWithLink();
        Assert.assertEquals(Integer.valueOf(2), countService.countWordsObervable(parsedPage).toBlocking().first());
    }

    @Test
    public void testWikiArticleBeingReadObservableWithRandomErrors() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Subscription subscription = wikiService.wikiArticleBeingReadObservableWithRandomErrors()
                .subscribe(articleName -> {
                            print(articleName);
                            assertNotNull(articleName);
                        },
                        error -> {
                            error.printStackTrace();
                            monitor.complete();
                        }
                );

        monitor.waitFor(5, TimeUnit.SECONDS);
        subscription.unsubscribe();
        Assert.assertTrue(monitor.isComplete());
    }

    @Test
    public void testWikiArticleBeingReadObservableBurst() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Subscription subscription = wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .subscribe(articleName -> {
                            print(articleName);
                            assertNotNull(articleName);
                        },
                        Throwable::printStackTrace,
                        monitor::complete
                );

        monitor.waitFor(5, TimeUnit.SECONDS);
        subscription.unsubscribe();
        Assert.assertTrue(monitor.isComplete());
    }
}