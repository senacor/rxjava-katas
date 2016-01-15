package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapi;
import org.junit.Test;

public class WikipediaServiceJapiTest {

    private final WikipediaServiceJapi wikipediaService = new WikipediaServiceJapi();

    @Test
    public void demonstrate() {
        String article = wikipediaService.getArticle("42");
        System.out.println("article=" + article);
    }

}