package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.apache.commons.collections4.map.LRUMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.senacor.tecco.codecamp.reactive.services.wikiloader.WikiControllerTest.EIGENWERT_ARTICLE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Andreas Keefer
 */
@RunWith(MockitoJUnitRunner.class)
public class WikiControllerMockTest {

    @Mock
    private Map<String, Article> cache;
    @Mock
    private CountService countService;
    @Mock
    private WikiService wikiService;
    @Mock
    private RatingService ratingService;

    @Test
    public void fetchArticleNoCacheHit() throws Exception {
        doReturn(Mono.just(EIGENWERT_ARTICLE.getContent()))
                .when(wikiService).fetchArticleNonBlocking(EIGENWERT_ARTICLE.getName());

        WikiController wikiController = new WikiController(wikiService, countService, ratingService, cache);

        Article article = wikiController.fetchArticle(EIGENWERT_ARTICLE.getName()).block();

        assertThat(article, is(EIGENWERT_ARTICLE));
        verify(cache, times(1)).get(EIGENWERT_ARTICLE.getName());
        verify(cache, times(1)).put(EIGENWERT_ARTICLE.getName(), EIGENWERT_ARTICLE);
        verify(wikiService, times(1)).fetchArticleNonBlocking(EIGENWERT_ARTICLE.getName());
    }

    @Test
    public void fetchArticleCacheHit() throws Exception {
        doReturn(EIGENWERT_ARTICLE).when(cache).get(EIGENWERT_ARTICLE.getName());
        doReturn(Mono.just("dummy"))
                .when(wikiService).fetchArticleNonBlocking(EIGENWERT_ARTICLE.getName());

        WikiController wikiController = new WikiController(wikiService, countService, ratingService, cache);

        Article article = wikiController.fetchArticle(EIGENWERT_ARTICLE.getName()).block();

        assertThat(article, is(EIGENWERT_ARTICLE));
        verify(cache, times(1)).get(EIGENWERT_ARTICLE.getName());
        verify(cache, times(0)).put(EIGENWERT_ARTICLE.getName(), EIGENWERT_ARTICLE);
        verify(wikiService, times(1)).fetchArticleNonBlocking(EIGENWERT_ARTICLE.getName());
    }

    @Test
    public void fetchArticleLRUMap() throws Exception {
        this.cache = Mockito.spy(new LRUMap<String, Article>(1));
        doReturn(Mono.just("dummy content"))
                .when(wikiService).fetchArticleNonBlocking("dummy");
        doReturn(Mono.just("test content"))
                .when(wikiService).fetchArticleNonBlocking("test");

        WikiController wikiController = new WikiController(wikiService, countService, ratingService, cache);

        Article article = wikiController.fetchArticle("dummy").block();
        assertThat(article.getContent(), is("dummy content"));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey("dummy"), is(true));
        assertThat(cache.containsKey("test"), is(false));

        article = wikiController.fetchArticle("dummy").block();
        assertThat(article.getContent(), is("dummy content"));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey("dummy"), is(true));
        assertThat(cache.containsKey("test"), is(false));

        article = wikiController.fetchArticle("test").block();
        assertThat(article.getContent(), is("test content"));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey("dummy"), is(false));
        assertThat(cache.containsKey("test"), is(true));

        article = wikiController.fetchArticle("dummy").block();
        assertThat(article.getContent(), is("dummy content"));
        assertThat(cache.size(), is(1));
        assertThat(cache.containsKey("dummy"), is(true));
        assertThat(cache.containsKey("test"), is(false));

        verify(cache, times(3)).get("dummy");
        verify(cache, times(2)).put(eq("dummy"), any(Article.class));

        verify(cache, times(1)).get("test");
        verify(cache, times(1)).put(eq("test"), any(Article.class));
        verify(wikiService, times(3)).fetchArticleNonBlocking("dummy");
        verify(wikiService, times(1)).fetchArticleNonBlocking("test");
    }
}
