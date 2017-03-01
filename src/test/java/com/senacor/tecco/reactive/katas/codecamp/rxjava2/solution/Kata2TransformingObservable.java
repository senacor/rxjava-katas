package com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.CountServiceImpl;
import com.senacor.tecco.reactive.services.WikiService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

    private final WikiService wikiService = new WikiService();
    private final CountService countService = CountServiceImpl.create();

    @Test
    public void transformingObservable() throws Exception {
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the text of the wikipedia article to the console (ParsedPage.getText())

        wikiService.fetchArticleObservable("Bilbilis")
                   //.doOnNext(debug -> print("fetchArticleObservable res: %s", debug))
                   .flatMap(wikiService::parseMediaWikiTextObservable)
                   //.doOnNext(debug -> print("parseMediaWikiTextObservable res: %s", debug))
                   .flatMapIterable(parsedPage -> Arrays.asList(StringUtils.split(parsedPage.getText(), " ")))
                   //.flatMap(parsedPage -> Observable.from(StringUtils.split(parsedPage.getText(), " ")))
                   .filter(word -> word.startsWith("a"))
                   .subscribe(next -> print("next: %s", next),
                           Throwable::printStackTrace);
    }


}
