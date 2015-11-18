package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.services.WikiService;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

    @Test
    public void transformingObservable() throws Exception {
        WIKI_SERVICE
                .fetchArticle("Java")
                .doOnNext(System.out::println)
                .flatMap(WIKI_SERVICE::parseMediaWikiText)
                .subscribe(System.out::println);
    }

}
