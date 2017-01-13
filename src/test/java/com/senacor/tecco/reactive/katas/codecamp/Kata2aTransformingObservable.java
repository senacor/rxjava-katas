package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2aTransformingObservable {

    private WikiService wikiService = new WikiService();

    @Test
    public void transformingObservable() throws Exception {
        wikiService.fetchArticleObservable("Observer")
                .map(wikiService::parseMediaWikiText)
                .subscribe(parsedPage -> System.out.println(parsedPage.getText()));
    }

}
