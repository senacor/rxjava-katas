package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import org.junit.Test;
import org.wikipedia.Wiki;
import rx.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

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

        WikiService.WIKI_SERVICE.fetchArticle("Bananen")
                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                .flatMap(page -> {
                    Observable<Section> sectionO = Observable.from(page.getSections());
                    Observable<Link> linkObservable = sectionO.flatMapIterable(section -> section.getLinks(Link.type.INTERNAL));
                    Observable<String> pageNameO= Observable.just(page.getName());
                    return Observable.combineLatest(pageNameO, linkObservable, (pnO, lo) -> pnO + "->"+ lo.getText());
                })
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        waitMonitor::complete);

        waitMonitor.waitFor(5, TimeUnit.SECONDS);

    }

}
