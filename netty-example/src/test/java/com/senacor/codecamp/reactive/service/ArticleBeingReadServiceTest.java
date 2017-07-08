package com.senacor.codecamp.reactive.service;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class ArticleBeingReadServiceTest {

    private static final WikiArticle ARTICLE = new WikiArticle("Eigenwert", "REDIRECT Eigenwertproblem", 5, 2);

    private ArticleBeingReadService service;

    @Before
    public void setUp() throws Exception {
        service = new ArticleBeingReadService(WikiService.create(DelayFunction.withNoDelay(),
                FlakinessFunction.noFlakiness(), true, "de"),
                RatingService.create(), CountService.create());
    }

    @Test
    //@Ignore
    public void createArticle() throws Exception {
        service.createArticle(ARTICLE.getName())
                .test()
                .assertNoErrors()
                .assertValue(ARTICLE)
                .assertComplete();
    }

}