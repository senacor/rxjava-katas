package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.services.WikiService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata2BasicOperators {

    private final WikiService wikiService = WikiService.create();

    @Test
    public void basicsA() throws Exception {
        // 1. Use the WikiService (fetchArticleFlux) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. split the Article (ParsedPage.getText()) in words (separator=" ")
        // 4. sum the number of letters of all words beginning with character 'a' to the console

        StepVerifier.create(
                wikiService.fetchArticleFlux("Physik")
                        //.doOnNext(debug -> print("fetchArticleFlux res: %s", debug))
                        .map(wikiService::parseMediaWikiText)
                        //.doOnNext(debug -> print("parseMediaWikiTextFlux res: %s", debug))
                        .flatMapIterable(parsedPage -> Arrays.asList(StringUtils.split(parsedPage.getText(), " ")))
                        //.flatMap(parsedPage -> Flux.from(StringUtils.split(parsedPage.getText(), " ")))
                        .filter(word -> word.startsWith("a"))
                        .doOnNext(next -> print("words starting with 'a': %s", next))
                        .reduce(0, (letterCount, word) -> letterCount + word.length())
                        .doOnSuccess(next -> print("letter count of 'a'-words: %s", next))
        )
                .expectNextMatches(value -> value > 1000)
                .verifyComplete();
    }

    @Test
    public void basicsB() throws Exception {
        // 5. filter out redundant words beginning with 'a'
        // 6. order them by length and take only the top 10 words in length

        StepVerifier.create(
                wikiService.fetchArticleFlux("Physik")
                        .map(wikiService::parseMediaWikiText)
                        .flatMapIterable(parsedPage -> Arrays.asList(StringUtils.split(parsedPage.getText(), " ")))
                        .filter(word -> word.startsWith("a"))
                        .distinct()
                        .sort((o1, o2) -> Integer.compare(o2.length(), o1.length()))
                        .take(10)
                        .doOnNext(next -> print("Top 10 words starting with 'a': %s", next))
                        .reduce(0, (letterCount, word) -> letterCount + word.length())
                        .doOnSuccess(next -> print("letter count of 'a'-words: %s", next))
        )
                .expectNextMatches(value -> value > 100)
                .verifyComplete();
    }
}