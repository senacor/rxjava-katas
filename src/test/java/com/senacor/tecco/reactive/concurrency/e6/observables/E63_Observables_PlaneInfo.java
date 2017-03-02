package com.senacor.tecco.reactive.concurrency.e6.observables;

import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.Observable;
import org.junit.Rule;
import org.junit.Test;

public class E63_Observables_PlaneInfo extends PlaneArticleBaseTest {

    private final CountService countService = CountService.create();
    private final RatingService ratingService = RatingService.create();

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneInfoIsCombinedWithObservables_notPerfectYet() throws Exception {
        getMetrixs("Boeing 777").subscribe(pageMetrix -> Summary
                .print("777", pageMetrix.words, pageMetrix.rating, pageMetrix.numberBuild)
        );

        getMetrixs("Boeing 747").subscribe(pageMetrix -> Summary
                .print("747", pageMetrix.words, pageMetrix.rating, pageMetrix.numberBuild)
        );
    }

    private Observable<PageMetrix> getMetrixs(String planeName) {
        Observable<String> article = wikiService.fetchArticleObservable(planeName);
        Observable<ParsedPage> parsedPage = article.flatMap(wikiService::parseMediaWikiTextObservable);

        Observable<Integer> ratings = parsedPage.flatMap(ratingService::rateObservable);
        Observable<Integer> wordCount = parsedPage.flatMap(countService::countWordsObservable);
        Observable<String> buildCount = article.map(this::parseBuildCount);

        return ratings.zipWith(wordCount, RatingWordCount::new).zipWith(buildCount, PageMetrix::new);
    }

    private static class RatingWordCount {
        final Integer rating, wordCount;

        RatingWordCount(Integer rating, Integer wordCount) {
            this.rating = rating;
            this.wordCount = wordCount;
        }
    }

    private static class PageMetrix {
        private final int words;
        private final int rating;
        private final String numberBuild;

        private PageMetrix(RatingWordCount rwc, String numberBuild) {
            this.words = rwc.wordCount;
            this.rating = rwc.rating;
            this.numberBuild = numberBuild;
        }
    }
}
