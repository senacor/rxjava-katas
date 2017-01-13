package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata2cTransformingObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();

    @Test
    public void transformingObservable() throws Exception {
        wikiService.fetchArticleObservable("Flugzeug")
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> Observable.from(parsedPage.getText().split(" ")))
                .filter(str -> str.charAt(0) == 'a').count()
                .subscribe(System.out::println);
    }

}
