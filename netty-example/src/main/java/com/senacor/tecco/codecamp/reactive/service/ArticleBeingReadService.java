package com.senacor.tecco.codecamp.reactive.service;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;


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
        //return wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .flatMap(articleName ->
                        wikiService.fetchArticleObservable(articleName).subscribeOn(Schedulers.io())
                                .flatMap(articleText -> wikiService.parseMediaWikiTextObservable(articleText)
                                        .flatMap(parsedPage -> Observable.zip(
                                                ratingService.rateObservable(parsedPage),
                                                countService.countWordsObservable(parsedPage),
                                                Observable.just(parsedPage).map(page -> {
                                                    String text;
                                                    if (page.getFirstParagraph() != null) {
                                                        text = page.getFirstParagraph().getText();
                                                    } else {
                                                        text = page.getText();
                                                    }
                                                    return text.substring(0, Math.min(text.length(), 250)) + "...";
                                                }),
                                                (rating, wordCount, content) -> new WikiArticle(articleName, content, rating, wordCount))
                                        )));

    }

}
