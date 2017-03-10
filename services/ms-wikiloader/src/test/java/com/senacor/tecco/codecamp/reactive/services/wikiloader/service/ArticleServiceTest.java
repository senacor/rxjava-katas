package com.senacor.tecco.codecamp.reactive.services.wikiloader.service;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import org.junit.Before;
import org.junit.Test;
import reactor.test.StepVerifier;

/**
 * @author Andreas Keefer
 */
public class ArticleServiceTest {

    static final Article EIGENWERT_ARTICLE = Article.newBuilder()
            .withName("Eigenwert")
            .withContent("#REDIRECT [[Eigenwertproblem]]")
            .build();

    private ArticleService articleService;

    @Before
    public void setUp() throws Exception {
        articleService = new ArticleService(WikiService.create(DelayFunction.withNoDelay()),
                CountService.create(DelayFunction.withNoDelay()),
                RatingService.create(DelayFunction.withNoDelay()),
                1);
    }

    @Test
    public void fetchArticle() throws Exception {
        StepVerifier.create(articleService.fetchArticle(EIGENWERT_ARTICLE.getName()))
                .expectNext(EIGENWERT_ARTICLE.getContent())
                .verifyComplete();
    }

    @Test
    public void rate() throws Exception {
        StepVerifier.create(articleService.rate(EIGENWERT_ARTICLE.getName()))
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void countWords() throws Exception {
        StepVerifier.create(articleService.countWords(EIGENWERT_ARTICLE.getName()))
                .expectNext(2)
                .verifyComplete();
    }

}