package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.integration.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata1CreateObservable {

    @Test
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle


    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
