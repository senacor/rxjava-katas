package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.nightmare;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private final WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(nightmare)
    public void linksObservable() throws Exception {
        WaitMonitor waitMonitor = new WaitMonitor();

        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)doOnEach
        // 3. if that works: add a recursion (only 1 step) that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice
        final String articleName = "Schnitzel";
        getInternalLinksFromArticle(articleName)
                .flatMap(
                        s ->
                                getInternalLinksFromArticle(s.getTarget())
                                        .map(u -> new LinkPair(s.getText(), u.getText()))
                                        .subscribeOn(Schedulers.io())
                )
                .distinct()
                .subscribe(System.out::println, Throwable::printStackTrace, waitMonitor::complete);


        waitMonitor.waitFor(5, TimeUnit.SECONDS);
    }

    private Observable<Link> getInternalLinksFromArticle(String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .map(wikiService::parseMediaWikiText)
                .flatMap(parsedPage -> Observable.fromIterable(parsedPage.getSections()))
                .flatMap(s -> Observable.fromIterable(s.getLinks(Link.type.INTERNAL)));
    }

}


class LinkPair {
    private String start;
    private String target;

    public LinkPair(String start, String target) {
        this.start = start;
        this.target = target;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return start + " -> " + target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkPair linkPair = (LinkPair) o;
        return Objects.equals(getStart(), linkPair.getStart()) &&
                Objects.equals(getTarget(), linkPair.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getTarget());
    }
}