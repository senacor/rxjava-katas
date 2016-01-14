package com.senacor.tecco.reactive.services;

import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

public class WikipediaServiceMediaWikiBotTest {

    private final WikipediaServiceMediaWikiBot wikipediaService = new WikipediaServiceMediaWikiBot();

    @Test
    public void demonstrate() {
        Article article = wikipediaService.getArticle("42");
        System.out.println("title=" + article.getTitle());
        System.out.println("editor=" + article.getEditor());
        System.out.println("text=" + article.getText());
    }

}