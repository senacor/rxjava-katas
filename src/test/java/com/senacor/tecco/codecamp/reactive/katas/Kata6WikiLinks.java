package com.senacor.tecco.codecamp.reactive.katas;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;
import static rx.Observable.defer;
import static rx.Observable.from;

import java.util.concurrent.TimeUnit;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;

import org.junit.Test;

import de.tudarmstadt.ukp.wikipedia.parser.Link;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    @Test
    public void linksObservable() throws Exception {
        // 1. lade und parse einen beliebigen Wiki Artikel
        // 2. mappe alle internen Links (parsedPage.getSections().getLinks(Link.type.INTERNAL)) des Artikel auf ein Observable und gib dies auf der Console aus (<Start_Artikel> -> <Link/Artikel_Name>)
        // 3. Wenn das Funktioniert füge eine Rekursion hinzu und lade alle Artikel zu den Links und gib wiederum alle Links auf der Console aus
        // 4. miss die Performance und optimiere die Performance mit Schedulern
        // 5. gib keine Kombination <Start_Artikel> -> <Link/Artikel_Name> mehrfach aus

        WaitMonitor waitMonitor = new WaitMonitor();

        String startArticle = "Observable";
        getLinksFromArticle(startArticle)
                .map(link -> link.getTarget())
                .distinct()
                .flatMap(articleName -> getLinksFromArticle(articleName))
                .map(link -> link.getTarget())
                .distinct()
                .subscribe(link -> System.out.println(startArticle + " -> " + link),
                        error -> error.printStackTrace(),
                        () -> waitMonitor.complete());

        waitMonitor.waitFor(50000L, TimeUnit.SECONDS);
    }

    private Observable<Link> getLinksFromArticle(String articleName) {
        return defer(() -> WIKI_SERVICE.fetchArticle(articleName).subscribeOn(Schedulers.io()))
                .flatMap(article -> WIKI_SERVICE.parseMediaWikiText(article).subscribeOn(Schedulers.computation()))
                .flatMap(page -> from(page.getSections()))
                .flatMap(section -> from(section.getLinks(Link.type.INTERNAL)));
    }

}
