package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import org.junit.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private WikiService wikiService = new WikiService();

    HashSet<String> alreadyDone = new HashSet<>();


    @Test
    public void linksObservable() throws Exception {
        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)doOnEach
        // 3. if that works: add a recursion that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice
        //wikiService.fetchArticleObservable(...);


        recursive("Boeing 777", 2);
    }

    private void recursive(String wikiArticle, int depth) {

        if (depth > 0 && !alreadyDone.contains(wikiArticle)) {
            alreadyDone.add(wikiArticle);
            getLinks(wikiArticle)
                .subscribe(n -> {
                    for (Link current : n) {
                    recursive(current.getTarget(), (depth-1));
                    }
                });
        }
    }

    private Observable<List<Link>> getLinks(String wikiArticle) {
        return Observable.create(subscriber -> {

            wikiService.fetchArticleObservable(wikiArticle)
                .map(article -> wikiService.parseMediaWikiText(article))
                .filter(page -> page != null && page.getLinks() != null)
                .flatMapIterable(page -> {

                    List<Link> links = new ArrayList<>();
                    for (Section current : page.getSections()) {

                        links.addAll(current.getLinks(Link.type.INTERNAL));
                        subscriber.onNext(current.getLinks(Link.type.INTERNAL));
                    }
                    return links;
                })
                .map(Link::getTarget)
                .subscribe(
                        n -> System.out.println("Start: " + wikiArticle + " -> Target: " + n),
                        subscriber::onError,
                        subscriber::onCompleted
                );
        });
    }
}
