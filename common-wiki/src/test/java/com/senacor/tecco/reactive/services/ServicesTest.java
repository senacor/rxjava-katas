package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.util.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Andreas Keefer
 */
public class ServicesTest {

    private final WikiService wikiService = WikiService.create();
    private final CountService countService = CountService.create();
    private final RatingService ratingService = RatingService.create();

    @Test
    public void testFetchArticle() throws Exception {
        String article = wikiService.fetchArticle("42");
        System.out.println(article);
        assertNotNull(article);
    }

    @Test
    public void testParseMediaWikiText() throws Exception {
        ParsedPage parsedPage = wikiService.parseMediaWikiText("== Weblinks ==");
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
        return wikiService.parseMediaWikiText("== Weblinks ==\n [[42]]");
    }

    @Test
    public void testWikiArticleBeingReadObservable() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Disposable Disposable = wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .subscribe(article -> {
                            print(article);
                            assertNotNull(article);
                        },
                        error -> {
                        }, monitor::complete
                );
        monitor.waitFor(1, TimeUnit.SECONDS);
        Disposable.dispose();
    }

    @Test
    public void testWikiArticleBeingReadObservableComplex() throws Exception {

        final WaitMonitor monitor = new WaitMonitor();
        final Scheduler scheduler = Schedulers.from(Executors.newFixedThreadPool(5));
        Disposable Disposable = wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS)
                .sample(200, TimeUnit.MILLISECONDS)
                .doOnNext(name -> print("=> working with " + name))
                .flatMap((wikiArticle) -> wikiService
                        .fetchArticleObservable(wikiArticle)
                        .subscribeOn(scheduler)
                        .zipWith(Observable
                                .just(wikiArticle), (text, name) -> new String[]{name, text}))
                .flatMap(array -> wikiService
                        .parseMediaWikiTextObservable(array[1])
                        .subscribeOn(Schedulers.computation())
                        .zipWith(Observable
                                .just(array[0]), (parsedPage, name) -> new Object[]{name, parsedPage}))
                .flatMap(array -> {
                    final String articleName = (String) array[0];
                    final ParsedPage parsedPage = (ParsedPage) array[1];
                    return Observable.zip(ratingService.rateObservable(parsedPage)
                                    .subscribeOn(scheduler)
                            , countService.countWordsObservable(parsedPage)
                                    .subscribeOn(scheduler)
                            , (rating, wordCount) -> String
                                    .format("{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                                            articleName, rating, wordCount));
                })
                .subscribe(analizedArticle -> {
                            print(analizedArticle);
                            assertNotNull(analizedArticle);
                        },
                        error -> {
                        }, monitor::complete
                );
        monitor.waitFor(5, TimeUnit.SECONDS);
        Disposable.dispose();
    }

    @Test
    public void testRate() throws Exception {
        ParsedPage parsedPage = getParseMediaWikiTextWithLink();
        Integer rating = ratingService.rate(parsedPage);
        assertEquals(5, rating.intValue());

        parsedPage = wikiService.parseMediaWikiText("== Weblinks ==\n[[42]] [[42]]");
        rating = ratingService.rate(parsedPage);
        assertEquals(5, rating.intValue());

        parsedPage = wikiService.parseMediaWikiText("== Weblinks ==");
        rating = ratingService.rate(parsedPage);
        assertEquals(0, rating.intValue());
    }

    @Test
    public void testCountWords() throws Exception {
        ParsedPage parsedPage = getParseMediaWikiTextWithLink();
        Assert.assertEquals(2L, countService.countWords(parsedPage));
    }

    @Test
    public void testWikiArticleBeingReadObservableWithRandomErrors() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Disposable Disposable = wikiService.wikiArticleBeingReadObservableWithRandomErrors()
                .subscribe(articleName -> {
                            print(articleName);
                            assertNotNull(articleName);
                        },
                        error -> {
                            error.printStackTrace();
                            monitor.complete();
                        }
                );

        monitor.waitFor(10, TimeUnit.SECONDS);
        Disposable.dispose();
        Assert.assertTrue(monitor.isComplete());
    }

    @Test
    public void testWikiArticleBeingReadObservableBurst() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Disposable Disposable = wikiService.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .subscribe(articleName -> {
                            print(articleName);
                            assertNotNull(articleName);
                        },
                        Throwable::printStackTrace,
                        monitor::complete
                );

        monitor.waitFor(10, TimeUnit.SECONDS);
        Disposable.dispose();
        Assert.assertTrue(monitor.isComplete());
    }
}