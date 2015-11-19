package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    @Test
    public void linksObservable() throws Exception {

        WaitMonitor monitor = new WaitMonitor();
        // 1. lade und parse einen beliebigen Wiki Artikel
        // 2. mappe alle internen Links (parsedPage.getSections().getLinks(Link.type.INTERNAL)) des Artikel auf ein Observable und gib dies auf der Console aus (<Start_Artikel> -> <Link/Artikel_Name>)doOnEach
        // 3. Wenn das Funktioniert füge eine Rekursion hinzu und lade alle Artikel zu den Links und gib wiederum alle Links auf der Console aus
        // 4. miss die Performance und optimiere die Performance mit Schedulern
        // 5. gib keine Kombination <Start_Artikel> -> <Link/Artikel_Name> mehrfach aus

        //WikiService.WIKI_SERVICE.fetchArticle(...);

        String startArtikel = "Rom";

        links(0,startArtikel)
                .flatMap(l -> links(1, l.getTarget()).subscribeOn(myScheduler()))
                .subscribe(out -> {
                }, e -> e.printStackTrace(), () -> monitor.complete());
        monitor.waitFor(30, TimeUnit.SECONDS);
    }

    private void doPrint(int level, String start, String target) {
         ReactiveUtil.print("Level: " + level + " " + start + " -> " + target);
    }

    private Observable<ParsedPage> parsedArticle(String name) {
        return WikiService.WIKI_SERVICE.fetchArticle(name)
                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                ;
    }

    private Observable<Link> links(int level, String pageName) {
        return parsedArticle(pageName)
                .flatMap(p -> Observable.from(p.getSections()))
                .flatMap(s -> Observable.from(s.getLinks(Link.type.INTERNAL)))
                .take(20)
                .doOnNext(l -> doPrint(level, pageName, l.getTarget()));
    }

    private Scheduler myScheduler() {
        return ReactiveUtil.newScheduler(50,"MyScheduler");
    }

}
