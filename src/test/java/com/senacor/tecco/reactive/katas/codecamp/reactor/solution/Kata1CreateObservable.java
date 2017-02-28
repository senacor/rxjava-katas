package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;
import reactor.core.publisher.Mono;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class Kata1CreateObservable {

    public static final String ARTICLE_NAME = "Observable";

    @Test
    public void erzeugeEinObservable() throws Exception {
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
    public void erzeugeEinObservable2() throws Exception {
        Mono.just(ARTICLE_NAME)
                .map(this::getArticle)
                .map(Article::getText)
                .subscribe(ReactiveUtil::print);
    }

    private Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
