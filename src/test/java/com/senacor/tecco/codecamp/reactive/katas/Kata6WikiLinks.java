package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    @Test
    public void linksObservable() throws Exception {
        final WaitMonitor waitMonitor = new WaitMonitor();
        String startArticle = "Pflaume";

        getLinksForArticle(startArticle)
                .flatMap(target -> getLinksForArticle(target))
                .distinct()
                .subscribe(
                        link -> {},
                        Throwable::printStackTrace,
                        () -> waitMonitor.complete()
                );

        waitMonitor.waitFor(20, TimeUnit.SECONDS);
    }

    private Observable<String> getLinksForArticle(final String articleName){
        //System.out.println("****: " + articleName);
        return WIKI_SERVICE.fetchArticle(articleName)
                .flatMap(article -> WikiService.WIKI_SERVICE.parseMediaWikiText(article))
                .flatMapIterable(page -> page.getSections())
                .flatMapIterable(section -> section.getLinks(Link.type.INTERNAL))
                .doOnNext(link -> System.out.println(articleName + " -> " + link.getTarget()))
                .map(link -> link.getTarget())
                .distinct();
    }
}
