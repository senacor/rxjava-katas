package com.senacor.codecamp.reactive.services;

import com.senacor.codecamp.reactive.services.integration.WikipediaServiceJapi;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceJapiImpl;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import org.junit.Assert;
import org.junit.Test;
import reactor.test.StepVerifier;

import static org.hamcrest.Matchers.is;

public class WikipediaServiceJapiImplTest {

    private final WikipediaServiceJapi wikipediaService = new WikipediaServiceJapiImpl();

    private final String ARTICLE_NAME = "Eigenwert";
    private final String ARTICLE_CONTENT = "#WEITERLEITUNG [[Eigenwertproblem]]\n" +
            "{{Wikidata-Weiterleitung|Q21406831}}\n" +
            "\n" +
            "[[Kategorie:Lineare Algebra]]";

    @Test
    public void getArticle() {
        String article = wikipediaService.getArticle(ARTICLE_NAME);
        Assert.assertThat(article, is(ARTICLE_CONTENT));
    }

    @Test
    public void getArticleNonBlocking() throws Exception {
        StepVerifier.create(
                wikipediaService.getArticleNonBlocking(ARTICLE_NAME)
                        .doOnNext(next -> ReactiveUtil.print("res getArticleNonBlocking: " + next))
        ).expectNext(ARTICLE_CONTENT)
                .verifyComplete();
    }

    @Test
    public void getArticleNonBlocking2() throws Exception {
        StepVerifier.create(
                wikipediaService.getArticleNonBlocking("Physik")
                        .doOnNext(next -> ReactiveUtil.print("res getArticleNonBlocking: " + next))
        ).expectNextMatches(article -> article.startsWith("{{Dieser Artikel|beschreibt die Naturwissenschaft Physik."))
                .verifyComplete();
    }
}