package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.integration.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata1Create {

    @Test
    public void create() throws Exception {
        final String articleName = "Observable";
        // Create an Flux from getArticle


    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
