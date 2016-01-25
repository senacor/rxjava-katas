package com.senacor.tecco.reactive.services;

import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapi;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapiImpl;
import org.junit.Test;

public class WikipediaServiceJapiImplTest {

    private final WikipediaServiceJapi wikipediaService = new WikipediaServiceJapiImpl();

    @Test
    public void demonstrate() {
        String article = wikipediaService.getArticle("42");
        System.out.println("article=" + article);
    }

}