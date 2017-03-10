package com.senacor.codecamp.reactive.katas.codecamp.reactor.solution;

import com.senacor.codecamp.reactive.services.integration.WikipediaServiceMediaWikiBot;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class Kata1Create {

    public static final String ARTICLE_NAME = "Observable";

    @Test
    public void create() throws Exception {
        Mono.<Article>create(sink -> {
            try {
                sink.success(getArticle(ARTICLE_NAME));
            } catch (Exception e) {
                sink.error(e);
            }
        }).subscribe(toPrint -> {
            ReactiveUtil.print(toPrint.getText());
        }, throwable -> System.err.println(throwable.getMessage()));
    }

    @Test
    public void createAlternative() throws Exception {
        StepVerifier.create(Mono.just(ARTICLE_NAME)
                .map(this::getArticle)
                .map(Article::getText))
                .expectNextMatches(value -> value.startsWith("Eine '''Observable'''"))
                .verifyComplete();
    }

    @Test
    public void createFlux() throws Exception {
        final String articleName = "Observable";

        StepVerifier.create(Flux.<Article>create(sink -> {
            try {
                sink.next(getArticle(articleName));
                sink.complete();
            } catch (Exception e) {
                sink.error(e);
            }
        }))
                .expectNextMatches(value -> articleName.equals(value.getTitle()))
                .verifyComplete();
    }

    private Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}