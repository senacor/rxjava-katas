package com.senacor.tecco.reactive.concurrency.e6.observables;

import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;

public class E63_Observables_PlaneInfo  extends PlaneArticleBaseTest {

    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneInfoIsCombinedWithObservables_notPerfectYet() throws Exception {


        fetchArticle("Boeing 777")
                .flatMap(article -> {
                    Observable<ParsedPage> parsedPageObservable = parsePage(article);
                    return parsedPageObservable.flatMap(page -> {
                        Observable<Integer> countObservable = countWords(page);
                        Observable<Integer> rateObservable = rateArticles(page);
                        String numberBuild = parseBuildCount(article);
                        return Observable.zip(countObservable, rateObservable, (words, rating) -> new PageMetrix(words, rating, numberBuild));
                    });
                })
                .subscribe(pageMetrix -> Summary.print("777", pageMetrix.words, pageMetrix.rating, pageMetrix.numberBuild));

        fetchArticle("Boeing 747")
                .flatMap(article -> {
                    Observable<ParsedPage> parsedPageObservable = parsePage(article);
                    return parsedPageObservable.flatMap(page -> {
                        Observable<Integer> countObservable = countWords(page);
                        Observable<Integer> rateObservable = rateArticles(page);
                        String numberBuild = parseBuildCount(article);
                        return Observable.zip(countObservable, rateObservable, (words, rating) -> new PageMetrix(words, rating, numberBuild));
                    });
                })
                .subscribe(pageMetrix -> Summary.print("747", pageMetrix.words, pageMetrix.rating, pageMetrix.numberBuild));
    }

    // fetches an article from Wikipedia
    private Observable<String> fetchArticle(String articleName) {
        return wikiService.fetchArticleObservable(articleName);
    }

    private Observable<ParsedPage> parsePage(String article777) {
        return wikiService.parseMediaWikiTextObservable(article777);
    }

    private Observable<Integer> countWords(ParsedPage parsedPage) {
        return countService.countWordsObervable(parsedPage);
    }

    private Observable<Integer> rateArticles(ParsedPage parsedPage) {
        return ratingService.rateObservable(parsedPage);
    }

    private final static class PageMetrix {
        private final int words;
        private final int rating;
        private final String numberBuild;

        private PageMetrix(int words, int rating, String numberBuild) {
            this.words = words;
            this.rating = rating;
            this.numberBuild = numberBuild;
        }

    }

}
