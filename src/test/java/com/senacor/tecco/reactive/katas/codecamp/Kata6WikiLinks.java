package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private WikiService wikiService = new WikiService();

    @Test
    public void linksObservable() throws Exception {
        TestSubscriber test = TestSubscriber.create();

        String articleName = "Flugzeug";

        getLinks(articleName)
                .flatMap(link -> getLinks(link.getTarget()).subscribeOn(Schedulers.io()))
                .subscribe(test);

        test.awaitTerminalEvent();

        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)doOnEach
        // 3. if that works: add a recursion that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice
        //wikiService.fetchArticleObservable(...);
    }

    public Observable<Link> getLinks(String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> Observable.from(parsedPage.getSections()))
                .flatMap(section -> Observable.from(section.getLinks(Link.type.INTERNAL)))
                .distinct()
                .doOnNext(link2 -> System.out.format("%s -> %s\n", articleName, link2.getTarget()));
    }

}
