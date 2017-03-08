package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ReplayProcessor;

import static com.senacor.tecco.reactive.util.DelayFunction.withNoDelay;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Daniel Heinrich
 * @since 08/03/2017
 */
@RunWith(MockitoJUnitRunner.class)
public class ReadEventTest {

    @Mock
    WikiService service;

    WikiController wikiController;

    @Before
    public void setup() {
        Mockito.when(service.fetchArticleNonBlocking(Mockito.anyString())).thenReturn(Mono.just(""));
        wikiController = new WikiController(service,
                CountService.create(withNoDelay()),
                RatingService.create(withNoDelay()),
                10);

    }

    @Test
    public void shouldEmitEventsOnRead() {
        ReplayProcessor<String> replay = subscribe();

        wikiController.fetchArticle("foo").subscribe();
        wikiController.fetchArticle("bar").subscribe();

        assertEvents(replay, "foo", "bar");
    }

    @Test
    public void shouldntEmitEventsPerDefault() {
        assertNoEvents(subscribe());
    }

    @Test
    public void shouldntEmitOldEvents() {
        wikiController.fetchArticle("ha").subscribe();
        wikiController.fetchArticle("haha").subscribe();
        assertNoEvents(subscribe());
    }

    @Test
    public void fetchingWordcountShouldNotEmitEvent() {
        ReplayProcessor<String> replay = subscribe();
        wikiController.getWordCount("foo").subscribe();
        assertNoEvents(replay);
    }

    @Test
    public void fetchingRatingShouldNotEmitEvent() {
        ReplayProcessor<String> replay = subscribe();
        wikiController.getRating("foo").subscribe();
        assertNoEvents(replay);
    }

    private ReplayProcessor<String> subscribe() {
        ReplayProcessor<String> articles = ReplayProcessor.create();
        wikiController.getReadStream()
                      .map(Article::getName)
                      .subscribe(articles);
        return articles;
    }

    private void assertNoEvents(ReplayProcessor<String> articles) {
        assertEvents(articles);
    }

    private void assertEvents(ReplayProcessor<String> articles, String... events) {
        articles.onComplete();
        assertEquals(asList(events), articles.collectList().block());
    }
}
