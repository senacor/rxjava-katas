package com.senacor.tecco.codecamp.reactive.katas;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;

import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import rx.Observable;

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

        final String articleName = "Liste der Listen deutschsprachiger Bezeichnungen nicht deutschsprachiger Orte";

        getLinksForArticle(articleName).subscribe( //
            links -> {
                links.forEach(link -> getLinksForArticle(link.getText()) //
                    .subscribe( //
                        innerLinks -> ReactiveUtil.print("done with %s", link.getText()), //
                        Throwable::printStackTrace));
                ReactiveUtil.print("done with %s", articleName);
            }, Throwable::printStackTrace);
    }

    private Observable<List<Link>> getLinksForArticle(String articleName) {
        return WIKI_SERVICE.fetchArticle(articleName) //
            .flatMap(WIKI_SERVICE::parseMediaWikiText) //
            .map(this::getLinksFromPage) //
            .distinct() //
            .doOnNext( //
                links -> links.forEach( //
                    link -> ReactiveUtil.print("%s -> %s", articleName, link.getText())));
    }

    private List<Link> getLinksFromPage(ParsedPage parsedPage) {
        return parsedPage //
            .getSections() //
            .stream() //
            .flatMap(section -> section //
                .getLinks(Link.type.INTERNAL) //
                .stream()) //
            .collect(Collectors.toList());
    }

}
