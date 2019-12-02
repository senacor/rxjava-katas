package com.senacor.codecamp.reactive.service;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.reactivex.rxjava3.core.Observable;


/**
 * @author Andri Bremm
 */
public class ArticleBeingReadService {

    private WikiService wikiService;
    private RatingService ratingService;
    private CountService countService;

    public ArticleBeingReadService() {
        this(WikiService.create(DelayFunction.staticDelay(30)), RatingService.create(), CountService.create());
    }

    public ArticleBeingReadService(WikiService wikiService, RatingService ratingService, CountService countService) {
        this.wikiService = wikiService;
        this.ratingService = ratingService;
        this.countService = countService;
    }

    public Observable<WikiArticle> articleBeingReadObservable() {
        return wikiService.wikiArticleBeingReadObservableBurst()
                // or return wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .flatMap(this::createArticle);
    }

    public Observable<WikiArticle> createArticle(String articleName) {
        /* Tasks:
         * 1. fetch the article using wikiService.fetchArticleObservable
         * Hint: there is an existing Test (ArticleBeingReadServiceTest) which is ignored. Remove the @Ignored
         *       Annotation and implement all 3 Tasks Test-Driven.
         * 2. parse the article using wikiService::parseMediaWikiTextObservable
         * 3. calculate a rating (ratingService.rate) and the word count (countService.countWords) for the article. Store the results in a WikiArticle object
         */

        return Observable.just(new WikiArticle(articleName, "Test", 1, 1));

    }

    private String getArticleShortText(ParsedPage parsedPage) {
        if (parsedPage.getText().length() < 100) {
            return parsedPage.getText();
        } else {
            return parsedPage.getText().substring(0, 99) + "...";
        }

    }
}