package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

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

        //WikiService.WIKI_SERVICE.fetchArticle(...);

        WaitMonitor waitMonitor = new WaitMonitor();

        final Observable<Link> links = getArticleLinks("Observable");

        links
                .subscribeOn(ReactiveUtil.newScheduler(10, "computeScheduler"))
                .flatMap(link -> getArticleLinks(link.getTarget()))
                .distinct()
                .subscribe(link -> System.out.println(link.getText()),
                        Throwable::printStackTrace,
                        () -> waitMonitor.complete());

        waitMonitor.waitFor(1L, TimeUnit.HOURS);

        while (!waitMonitor.isComplete()) {
            Thread.sleep(100L);
        }

        Thread.sleep(200L);

    }

    private Observable<Link> getArticleLinks(String wikiArticle) {
        return WikiService.WIKI_SERVICE.fetchArticle(wikiArticle)
                .subscribeOn(ReactiveUtil.newScheduler(10, "fetchScheduler"))
                .<ParsedPage>flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                .flatMapIterable(ParsedPage::getSections)
                .flatMapIterable(section -> section.getLinks(Link.type.INTERNAL))
                .distinct();
    }

}
