package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    @Test
    public void linksObservable() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();
        Scheduler fiftyThreads = ReactiveUtil.newScheduler(50, "my-scheduler");

        WIKI_SERVICE
                .fetchArticle("Java (Programmiersprache)")
                .flatMap(WIKI_SERVICE::parseMediaWikiText)
                .flatMapIterable(ParsedPage::getSections)
                .flatMapIterable(section -> section.getLinks(Link.type.INTERNAL))
                .map(link -> link.getText())
                .flatMap(name ->
                        WIKI_SERVICE
                                .fetchArticle(name)
                          //      .subscribeOn(fiftyThreads)
                )
                .subscribe(
                        p -> System.out.println(StringUtils.left(p, 20)),
                        e -> e.printStackTrace(),
                        waitMonitor::complete);

        // 1. lade und parse einen beliebigen Wiki Artikel
        // 2. mappe alle internen Links (parsedPage.getSections().getLinks(Link.type.INTERNAL)) des Artikel auf ein Observable und gib dies auf der Console aus (<Start_Artikel> -> <Link/Artikel_Name>)doOnEach
        // 3. Wenn das Funktioniert füge eine Rekursion hinzu und lade alle Artikel zu den Links und gib wiederum alle Links auf der Console aus
        // 4. miss die Performance und optimiere die Performance mit Schedulern
        // 5. gib keine Kombination <Start_Artikel> -> <Link/Artikel_Name> mehrfach aus

        //WikiService.WIKI_SERVICE.fetchArticle(...);
        waitMonitor.waitFor(50, TimeUnit.SECONDS);
    }

}
