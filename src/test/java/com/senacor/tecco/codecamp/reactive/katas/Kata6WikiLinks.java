package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    Scheduler scheduler = Schedulers.io();

    @Test
    public void linksObservable() throws Exception {
        // 1. lade und parse einen beliebigen Wiki Artikel
        // 2. mappe alle internen Links (parsedPage.getSections().getLinks(Link.type.INTERNAL)) des Artikel auf ein Observable und gib dies auf der Console aus (<Start_Artikel> -> <Link/Artikel_Name>)
        // 3. Wenn das Funktioniert füge eine Rekursion hinzu und lade alle Artikel zu den Links und gib wiederum alle Links auf der Console aus
        // 4. miss die Performance und optimiere die Performance mit Schedulern
        // 5. gib keine Kombination <Start_Artikel> -> <Link/Artikel_Name> mehrfach aus

        WaitMonitor monitor = new WaitMonitor();
        linksObservable("Observable", 0);
        monitor.waitFor(10, TimeUnit.SECONDS);
    }

    private void linksObservable(String articleName, int level) {

        if (level > 1) {
            return;
        }

        WikiService.WIKI_SERVICE.fetchArticle(articleName).subscribeOn(Schedulers.io())
                .flatMap(articleString -> WikiService.WIKI_SERVICE.parseMediaWikiText(articleString))
                .flatMap(page -> Observable.from(() -> page.getSections().iterator()))
                .flatMap(section -> Observable.from(() -> section.getLinks().iterator()))
                .distinct(link -> link.getTarget())
                .doOnNext(item -> System.out.println(
                        StringUtils.repeat("  ", level) + articleName + " -> " + item.getTarget()))
                .subscribe(
                        result -> linksObservable(result.getTarget(), level + 1)
                );
    }

}
