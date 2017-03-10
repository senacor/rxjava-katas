package com.senacor.codecamp.reactive.katas.codecamp.reactor;

import com.senacor.codecamp.reactive.services.integration.WikipediaServiceMediaWikiBot;
import com.senacor.codecamp.reactive.katas.KataClassification;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

/**
 * @author Andreas Keefer
 */
public class Kata1Create {

    @Test
    @KataClassification(mandatory)
    public void create() throws Exception {
        final String articleName = "Observable";
        // Create an Flux from getArticle


    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
