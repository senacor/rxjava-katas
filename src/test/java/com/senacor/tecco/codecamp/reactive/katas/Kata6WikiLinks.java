package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import org.junit.Test;
import rx.Observable;

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

        final Observable<Link> links = WikiService.WIKI_SERVICE.fetchArticle("Mathematik")
                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText)
                .flatMapIterable(ParsedPage::getSections)
                .<Section>asObservable()
                .flatMapIterable(section -> section.getLinks(Link.type.INTERNAL))
                .distinct()
                .asObservable();

        final Observable<ParsedPage> articles = links
                .flatMap(link -> WikiService.WIKI_SERVICE.fetchArticle(link.getTarget()))
                .flatMap(WikiService.WIKI_SERVICE::parseMediaWikiText);


        articles.subscribe(System.out::println, Throwable::printStackTrace, () -> waitMonitor.complete());

        Thread.sleep(10000L);
    }

}
