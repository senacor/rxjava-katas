package com.senacor.codecamp.reactive.service;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import io.reactivex.Observable;


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
        /* Tasks:
         * 1. Fetch the article, parse it and add the text it to the WikiArticle object.
         * 2. Calculate a rating for the article.
         * 3. Calculate the word count for the article.
         * 4. Test your implementation with wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS) and reduce millis to 10.
         */
        return wikiService.wikiArticleBeingReadObservableBurst()
        // or return wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .map(name -> new WikiArticle(name, "", 0, 0));

    }

}
