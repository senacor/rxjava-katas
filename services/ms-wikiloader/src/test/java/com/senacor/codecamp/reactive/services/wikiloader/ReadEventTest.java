package com.senacor.codecamp.reactive.services.wikiloader;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.codecamp.reactive.services.wikiloader.service.ArticleService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ReplayProcessor;

import static com.senacor.codecamp.reactive.util.DelayFunction.withNoDelay;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Daniel Heinrich
 * @since 08/03/2017
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class ReadEventTest {

    @Mock
    private WikiService service;

    private WikiController wikiController;

    @Before
    public void setup() {
        Mockito.when(service.fetchArticleNonBlocking(Mockito.anyString())).thenReturn(Mono.just(""));
        wikiController = new WikiController(new ArticleService(service,
                CountService.create(withNoDelay()),
                RatingService.create(withNoDelay()),
                10));

    }

    @Test
    // TODO Sprint2: activate after signature change
    public void shouldEmitEventsOnRead() throws InterruptedException {
//        ReplayProcessor<String> replay = subscribe();
//
//        wikiController.fetchArticle("foo").subscribe();
//        wikiController.fetchArticle("bar").subscribe();
//
//        Thread.sleep(WikiController.BUFFER_READ_EVENTS + 50);
//
//        assertEvents(replay, "foo", "bar");
    }

    @Test
    public void shouldntEmitEventsPerDefault() {
        assertNoEvents(subscribe());
    }

    @Test
    // TODO Sprint2: activate after signature change
    public void shouldntEmitOldEvents() {
//        wikiController.fetchArticle("ha").subscribe();
//        wikiController.fetchArticle("haha").subscribe();
//        assertNoEvents(subscribe());
    }

    @Test
    public void fetchingWordcountShouldNotEmitEvent() throws InterruptedException {
        ReplayProcessor<String> replay = subscribe();
        wikiController.getWordCount("foo").subscribe();

        Thread.sleep(WikiController.BUFFER_READ_EVENTS + 50);

        assertNoEvents(replay);
    }

    @Test
    public void fetchingRatingShouldNotEmitEvent() throws InterruptedException {
        ReplayProcessor<String> replay = subscribe();
        wikiController.getRating("foo").subscribe();

        Thread.sleep(WikiController.BUFFER_READ_EVENTS + 50);

        assertNoEvents(replay);
    }

    private ReplayProcessor<String> subscribe() {
        ReplayProcessor<String> articles = ReplayProcessor.create();
        // TODO Sprint2: remove Flux.just(...) after signature change
        Flux.just(
                wikiController.getReadStream()
        )
                .flatMap(Flux::fromIterable)
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
