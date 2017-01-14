package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {
    private final WikiService wikiService = new WikiService();
    private final int maxDepth = 1;

    @Test
    public void linksObservable() throws Exception {
        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)doOnEach
        // 3. if that works: add a recursion that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice
        //wikiService.fetchArticleObservable(...);

        long time = System.currentTimeMillis();
        final String articleName = "Java";
        linksObservable(articleName, 0);
        System.out.println("Execution took " + (System.currentTimeMillis() - time) + "ms");
    }

    private void linksObservable(String target, int depth) {
        if (depth > maxDepth) {
            return;
        }
        List<String> links = new ArrayList<>();
        wikiService.fetchArticleObservable(target)
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .filter(parsedPage -> parsedPage != null && parsedPage.getLinks() != null)
                .flatMapIterable(parsedPage -> parsedPage.getLinks())
                .filter(link -> link.getType().equals(Link.type.INTERNAL))
                .map(link -> link.getTarget())
                /*.flatMap(link -> Observable.just(link.getTarget())
                        .observeOn(Schedulers.io())
                )*/
                .distinct()
                .subscribe(link -> {
                            links.add(link);
                            linksObservable(link, depth + 1);
                        }, e -> e.printStackTrace(),
                        () -> printLinks(target, depth, links));
    }

    private void printLinks(String article, int depth, List<String> links) {
        System.out.println("Article: " + article + " (depth: " + depth + ")");
        for (String link : links) {
            System.out.println(article + " links to " + link);
        }
    }
}
