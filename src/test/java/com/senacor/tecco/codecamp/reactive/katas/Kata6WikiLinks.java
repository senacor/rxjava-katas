package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import org.junit.Test;
import rx.Scheduler;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    @Test
    public void linksObservable() throws Exception {
        // 1. lade und parse einen beliebigen Wiki Artikel
        // 2. mappe alle internen Links (parsedPage.getSections().getLinks(Link.type.INTERNAL)) des Artikel auf ein Observable und gib dies auf der Console aus (<Start_Artikel> -> <Link/Artikel_Name>)doOnEach
        // 3. Wenn das Funktioniert füge eine Rekursion hinzu und lade alle Artikel zu den Links und gib wiederum alle Links auf der Console aus
        // 4. miss die Performance und optimiere die Performance mit Schedulern
        // 5. gib keine Kombination <Start_Artikel> -> <Link/Artikel_Name> mehrfach aus

        //WikiService.WIKI_SERVICE.fetchArticle(...);

        Scheduler myScheduler = ReactiveUtil.newScheduler(50, "myScheduler");
        final WaitMonitor monitor = new WaitMonitor();

        fetchPageAndLinks("Java", myScheduler);


        monitor.waitFor(10L, TimeUnit.SECONDS);
        //.subscribeOn()

    }

    private void fetchPageAndLinks(String wikiArticle, Scheduler sd) {
        WikiService.WIKI_SERVICE.fetchArticle(wikiArticle).flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText).flatMapIterable(page -> {
           return page.getSections();
        }).subscribeOn(sd).flatMapIterable(section -> {
            return section.getLinks();
        }).filter(link -> {
            return Link.type.INTERNAL.equals(link.getType());
        }).distinct(link -> {
            return link.getTarget();
        }).subscribe(article -> {
            fetchPageAndLinks(article.getTarget(), sd);
        });
    }

}
